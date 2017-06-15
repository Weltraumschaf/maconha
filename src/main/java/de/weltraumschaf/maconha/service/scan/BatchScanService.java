package de.weltraumschaf.maconha.service.scan;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.service.ScanService;
import de.weltraumschaf.maconha.service.ScanServiceFactory;
import de.weltraumschaf.maconha.service.ScanStatusService;
import de.weltraumschaf.maconha.service.scan.batch.JobParameterKeys;
import de.weltraumschaf.maconha.service.scan.batch.ScanJobExecutionListener;
import org.joda.time.DateTime;
import org.joda.time.Duration;
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
import java.util.stream.Collectors;

/**
 * Batch job based implementation.
 * <p>
 * This class is package private because it must not be used directly from outside. Use the DI of Spring to get an
 * instance.
 * </p>
 */
@Service(ScanServiceFactory.BATCH)
final class BatchScanService extends BaseScanService implements ScanService, ScanCallBack {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchScanService.class);

    private final JobLauncher launcher;
    private final JobOperator operator;
    private final JobExplorer explorer;
    private final Job job;
    private final ScanJobExecutionListener listener;

    @Autowired
    BatchScanService(@Qualifier("asyncJobLauncher") final JobLauncher launcher, final JobOperator operator, final JobExplorer explorer, @Qualifier(JOB_NAME) final Job job, final ScanJobExecutionListener listener, final ScanStatusService statuses) {
        super(statuses);
        this.launcher = launcher;
        this.operator = operator;
        this.explorer = explorer;
        this.job = job;
        this.listener = listener;
    }

    @PostConstruct
    public void initHook() {
        LOGGER.debug("Initialize batch based scan service.");
        listener.register(this);
    }

    @Override
    public void scan(final Bucket bucket, final UI currentUi) {
        Validate.notNull(bucket, "bucket");
        Validate.notNull(currentUi, "currentUi");
        LOGGER.debug("Scan bucket with id {} and directory {} ...", bucket.getId(), bucket.getDirectory());

        try {
            final JobParameters parameters = createJobParameters(bucket);
            final Long id = launcher.run(job, parameters).getId();
            scans.put(id, new Execution(id, bucket, currentUi));
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
        final List<ScanStatus> allScans = scans.values().stream().map(execution -> {
            final JobExecution jobExecution = explorer.getJobExecution(execution.getId());
            return convert(jobExecution);
        }).collect(Collectors.toList());
        allScans.addAll(statuses.allStatuses());
        return allScans;
    }

    @Override
    public void beforeScan(final long id) {
        LOGGER.debug("Service called back before job execution with id {}.", id);
        final Bucket bucket = getExecution(id).getBucket();

        notifyClient(
            id,
            "Scan job started",
            "Scan for bucket '%s' in directory '%s' with id %d started.",
            bucket.getName(), bucket.getDirectory(), id);
    }

    @Override
    public void afterScan(final long id) {
        final JobExecution jobExecution = explorer.getJobExecution(id);
        LOGGER.debug("Service called back after job execution with id {}.", id);

        final Bucket bucket = getExecution(id).getBucket();
        final DateTime startTime = new DateTime(jobExecution.getStartTime());
        final DateTime endTime = new DateTime(jobExecution.getEndTime());
        final String duration = formatDuration(startTime, endTime);

        notifyClient(
            id,
            "Scan job finished",
            "Scan for bucket '%s' in directory '%s' with id %d finished in %s.",
            bucket.getName(), bucket.getDirectory(), id, duration);

        statuses.storeStatus(convert(jobExecution));
        scans.remove(id);
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
            formattedEndTime = formatDateTime(endTime);
        }

        return new ScanStatus(
            jobExecution.getJobId(),
            scans.get(jobExecution.getJobId()).getBucket().getName(),
            formatDateTime(jobExecution.getCreateTime()),
            formatDateTime(startTime),
            formattedEndTime,
            formatDuration(startTime, endTime),
            jobExecution.getStatus().name(),
            jobExecution.getExitStatus().getExitCode(),
            jobExecution.getAllFailureExceptions(),
            ScanServiceFactory.BATCH);
    }

    private void notifyClient(final Long jobId, final String caption, final String description, final Object... args) {
        final Notification notification = UiNotifier.notification(caption, description, args);
        UiNotifier.notifyClient(jobId, notification, getExecution(jobId).getCurrentUi());
    }

}
