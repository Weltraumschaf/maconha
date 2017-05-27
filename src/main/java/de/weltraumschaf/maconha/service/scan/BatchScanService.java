package de.weltraumschaf.maconha.service.scan;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.service.ScanService;
import de.weltraumschaf.maconha.service.ScanServiceFactory;
import de.weltraumschaf.maconha.service.scan.batch.JobParameterKeys;
import de.weltraumschaf.maconha.service.scan.batch.ScanJobExecutionListener;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
import javax.annotation.PreDestroy;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Batch job based implementation.
 * <p>
 * This class is package private because it must not be used directly from outside. Use the DI of Spring to get an
 * instance.
 * </p>
 */
@Service(ScanServiceFactory.BATCH)
final class BatchScanService extends BaseScanService implements ScanService, ScanJobExecutionListener.CallBack {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchScanService.class);

    private final DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("HH:mm:ss MM.dd.yy");

    private final Collection<ScanStatus> statuses = new CopyOnWriteArrayList<>();
    private final Map<Long, Execution> scans = new ConcurrentHashMap<>();
    private final JobLauncher launcher;
    private final JobOperator operator;
    private final JobExplorer explorer;
    private final Job job;
    private final ScanJobExecutionListener listener;
    private final MaconhaConfiguration config;

    @Autowired
    BatchScanService(@Qualifier("asyncJobLauncher") final JobLauncher launcher, final JobOperator operator, final JobExplorer explorer, @Qualifier(JOB_NAME) final Job job, final ScanJobExecutionListener listener, final MaconhaConfiguration config) {
        super();
        this.launcher = launcher;
        this.operator = operator;
        this.explorer = explorer;
        this.job = job;
        this.listener = listener;
        this.config = config;
    }

    @PostConstruct
    public void init() {
        LOGGER.debug("Initialize batch based scan service.");
        listener.register(this);
        final Path stausFile = resolveStausFile();

        if (Files.exists(stausFile)) {
            try (final Reader reader = Files.newBufferedReader(stausFile)) {
                LOGGER.debug("Loading stored statuses.");
                final Type type = new TypeToken<ArrayList<ScanStatus>>() {
                }.getType();
                statuses.addAll(new Gson().fromJson(reader, type));
            } catch (final IOException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        } else {
            LOGGER.debug("There is no such status file '{}' to load.", stausFile);
        }
    }


    @PreDestroy
    public void deinit() {
        final Path stausDir = resolveStausDir();

        if (!Files.exists(stausDir)) {
            LOGGER.debug("Create directory '{}' to store status file.", stausDir);

            try {
                Files.createDirectories(stausDir);
            } catch (IOException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }

        try (final BufferedWriter writer = Files.newBufferedWriter(resolveStausFile())) {
            LOGGER.debug("Store statuses.");
            new Gson().toJson(statuses, writer);
        } catch (final IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    private Path resolveStausFile() {
        return resolveStausDir().resolve("statuses.json");
    }

    private Path resolveStausDir() {
        return Paths.get(config.getHomedir()).resolve("scans");
    }

    @Override
    public void scan(final Bucket bucket, final UI currentUi) {
        Validate.notNull(bucket, "bucket");
        Validate.notNull(currentUi, "currentUi");
        LOGGER.debug("Scan bucket with id {} and directory {} ...", bucket.getId(), bucket.getDirectory());

        try {
            final JobParameters parameters = createJobParameters(bucket);

            final Execution execution = new Execution(launcher.run(job, parameters).getId(), bucket, currentUi);
            scans.put(execution.id, execution);
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
        final List<ScanStatus> running = scans.values().stream().map(execution -> {
            final JobExecution jobExecution = explorer.getJobExecution(execution.id);
            return convert(jobExecution);
        }).collect(Collectors.toList());
        running.addAll(statuses);
        return running;
    }

    @Override
    public void beforeJob(final JobExecution jobExecution) {
        final Long jobId = jobExecution.getJobId();
        LOGGER.debug("Service called back before job execution with id {}.", jobId);
        final Bucket bucket = getExecution(jobId).bucket;

        notifyClient(
            jobId,
            "Scan job started",
            "Scan for bucket '%s' in directory '%s' with id %d started.",
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
            "Scan for bucket '%s' in directory '%s' with id %d finished in %s.",
            bucket.getName(), bucket.getDirectory(), jobId, duration);

        statuses.add(convert(jobExecution));
        scans.remove(jobId);
    }

    private ScanStatus convert(final JobExecution jobExecution) {
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
            jobExecution.getJobId(),
            scans.get(jobExecution.getJobId()).bucket.getName(),
            dateTimeFormat.print(new DateTime(jobExecution.getCreateTime())),
            dateTimeFormat.print(startTime),
            formattedEndTime,
            secondsFormat.print(duration.toPeriod()),
            jobExecution.getStatus().name(),
            jobExecution.getExitStatus().getExitCode(),
            jobExecution.getAllFailureExceptions()
        );
    }

    private void notifyClient(final Long jobId, final String caption, final String description, final Object... args) {
        final Notification notification = notification(caption, description, args);
        notifyClient(jobId, notification, getExecution(jobId).currentUi);
    }

    private Execution getExecution(final Long jobId) {
        if (scans.containsKey(jobId)) {
            return scans.get(jobId);
        }

        throw new ScanError("There's no such job with id %d!", jobId);
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