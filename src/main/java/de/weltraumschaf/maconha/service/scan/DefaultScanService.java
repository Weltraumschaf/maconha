package de.weltraumschaf.maconha.service.scan;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.service.ScanService;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Default implementation.
 * <p>
 * This class is package private because it must not be used directly from outside. Use the DI of Spring to get an
 * instance.
 * </p>
 */
@Service
final class DefaultScanService implements ScanService, ScanJobExecutionListener.CallBack {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultScanService.class);

    private final PeriodFormatter secondsFormat =
        new PeriodFormatterBuilder()
            .printZeroAlways()
            .minimumPrintedDigits(2)
            .appendHours()
            .appendSeparator(":")
            .appendMinutes()
            .appendSeparator(":")
            .appendSeconds()
            .toFormatter();
    private final DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("HH:mm:ss MM.dd.yy");

    private final Map<Long, Execution> scans = new ConcurrentHashMap<>();
    private final JobLauncher launcher;
    private final JobOperator operator;
    private final JobExplorer explorer;
    private final Job job;
    private final ScanJobExecutionListener listener;

    @Autowired
    DefaultScanService(@Qualifier("asyncJobLauncher") final JobLauncher launcher, final JobOperator operator, final JobExplorer explorer, @Qualifier(JOB_NAME) final Job job, final ScanJobExecutionListener listener) {
        super();
        this.launcher = launcher;
        this.operator = operator;
        this.explorer = explorer;
        this.job = job;
        this.listener = listener;
    }

    @PostConstruct
    public void registerOnListener() {
        listener.register(this);
    }

    @Override
    public Long scan(final Bucket bucket, final UI currentUi) {
        Validate.notNull(bucket, "bucket");
        Validate.notNull(currentUi, "currentUi");
        LOGGER.debug("Scan bucket with id {} and directory {} ...", bucket.getId(), bucket.getDirectory());

        try {
            final JobParameters parameters = createJobParameters(bucket);

            final Execution execution = new Execution(launcher.run(job, parameters).getId(), bucket, currentUi);
            scans.put(execution.id, execution);
            return execution.id;
        } catch (final JobInstanceAlreadyCompleteException e) {
            throw new ScanError(e, "Job with name '%s' already completed!", JOB_NAME);
        } catch (final JobExecutionAlreadyRunningException e) {
            throw new ScanError(e, "Job with name '%s' already running!", JOB_NAME);
        } catch (final JobParametersInvalidException e) {
            throw new ScanError(e, "Parameters for job '%s' are invalid!", JOB_NAME);
        } catch (final JobRestartException e) {
            throw new ScanError(e, "Got restart exception for job '%s'!", JOB_NAME);
        }
    }

    private JobParameters createJobParameters(final Bucket bucket) {
        // The job framework does not allow to use arbitrary objects parameters so the bare values are used here.
        return new JobParametersBuilder()
            .addLong(JobParameterKeys.START_TIME, System.currentTimeMillis())
            .addLong(JobParameterKeys.BUCKET_ID, bucket.getId())
            .addString(JobParameterKeys.BUCKET_DIRECTORY, bucket.getDirectory())
            .toJobParameters();
    }

    @Override
    public boolean stop(final long executionId) {
        LOGGER.debug("Stop JobExecution with id: " + executionId);
        try {
            return operator.stop(executionId);
        } catch (final NoSuchJobExecutionException e) {
            throw new ScanError(e, "No job execution present for id %d!", executionId);
        } catch (final JobExecutionNotRunningException e) {
            throw new ScanError(e, "Job execution for id %d not running!", executionId);
        }
    }

    @Override
    public List<ScanStatus> overview() {
        return scans.values().stream().map(execution -> {
            final JobExecution jobExecution = explorer.getJobExecution(execution.id);
            final DateTime startTime = new DateTime(jobExecution.getStartTime());
            final DateTime endTime;
            final String formattedEndTime;

            if (jobExecution.getEndTime() == null) {
                endTime = DateTime.now();
                formattedEndTime = "-";
            } else {
                endTime = new DateTime(jobExecution.getEndTime());
                formattedEndTime = dateTimeFormat.print(endTime);
            }

            final Duration duration = new Duration(startTime, endTime);
            return new ScanStatus(
                execution.id,
                execution.bucket.getName(),
                dateTimeFormat.print(new DateTime(jobExecution.getCreateTime())),
                dateTimeFormat.print(startTime),
                formattedEndTime,
                secondsFormat.print(duration.toPeriod()),
                jobExecution.getStatus().name(),
                jobExecution.getExitStatus().getExitCode(),
                jobExecution.getAllFailureExceptions()
            );
        }).collect(Collectors.toList());
    }

    @Override
    public void beforeJob(final JobExecution jobExecution) {
        final Long jobId = jobExecution.getJobId();
        LOGGER.debug("Service called back before job execution with id {}.", jobId);
        final Bucket bucket = getExecution(jobId).bucket;

        notifyClient(
            jobId,
            "Scan job started",
            "Scan job for bucket '%s' in directory '%s' with id %d started.",
            bucket.getName(), bucket.getDirectory(), jobId);
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        final Long jobId = jobExecution.getJobId();
        LOGGER.debug("Service called back after job execution with id {}.", jobId);

        final Bucket bucket = getExecution(jobId).bucket;
        final DateTime startTime = new DateTime(jobExecution.getStartTime());
        final DateTime endTime = new DateTime(jobExecution.getEndTime());
        final String duration = secondsFormat.print(new Duration(startTime, endTime).toPeriod());

        notifyClient(
            jobId,
            "Scan job finished",
            "Scan job for bucket '%s' in directory '%s' with id %d finished in %s.",
            bucket.getName(), bucket.getDirectory(), jobId, duration);
    }

    private void notifyClient(final Long jobId, final String caption, final String description, final Object... args) {
        final Notification notification = notification(caption, description, args);
        final Execution execution = getExecution(jobId);
        final UI ui = execution.currentUi;

        if (ui == null) {
            LOGGER.warn("Currents UI null! Can't notify client about job with id {}.", jobId);
        } else {
            ui.access(() -> {
                final Page page = ui.getPage();

                if (page == null) {
                    LOGGER.warn("Currents page null! Can't notify client about job with id {}.", jobId);
                } else {
                    notification.show(page);
                }
            });
        }
    }

    private Execution getExecution(final Long jobId) {
        if (scans.containsKey(jobId)) {
            return scans.get(jobId);
        }

        throw new ScanError("There's no such job with id %d!", jobId);
    }

    private Notification notification(final String caption, final String description, final Object... args) {
        return new Notification(caption, String.format(description, args), Notification.Type.TRAY_NOTIFICATION);
    }

    /**
     * Data structure to remember started scan jobs.
     */
    private final class Execution {
        private Long id;
        private final Bucket bucket;
        private UI currentUi;

        Execution(final Long id, final Bucket bucket, final UI currentUi) {
            super();
            this.id = id;
            this.bucket = bucket;
            this.currentUi = currentUi;
        }

        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Execution)) {
                return false;
            }

            final Execution execution = (Execution) o;
            return Objects.equals(id, execution.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "Execution{" +
                "id=" + id +
                ", bucket=" + bucket +
                ", currentUi=" + currentUi +
                '}';
        }
    }
}
