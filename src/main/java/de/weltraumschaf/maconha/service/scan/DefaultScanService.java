package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.service.ScanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation.
 * <p>
 * This class is package private because it must not be used directly from outside. Use the DI of Spring to get an
 * instance.
 * </p>
 */
@Service
final class DefaultScanService implements ScanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultScanService.class);

    private final Map<Long, ScanStatus> scans = new ConcurrentHashMap<>();
    private final JobLauncher launcher;
    private final JobOperator operator;
    private final JobExplorer explorer;
    private final Job job;

    @Autowired
    DefaultScanService(@Qualifier("asyncJobLauncher") final JobLauncher launcher, final JobOperator operator, final JobExplorer explorer, @Qualifier(JOB_NAME) final Job job) {
        super();
        this.launcher = launcher;
        this.operator = operator;
        this.explorer = explorer;
        this.job = job;
    }

    @Override
    public Long scan(final Bucket bucket)  {
        Validate.notNull(bucket, "bucket");
        LOGGER.debug("Scan bucket with id {} and directory {} ...", bucket.getId(), bucket.getDirectory());

        try {
            final JobParameters parameters = createJobParameters(bucket);
            final ScanStatus status = new ScanStatus(launcher.run(job, parameters).getId());
            scans.put(status.getId(), status);
            return status.getId();
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
        // The job framework does not allow to use arbitrary objects s parameters so the bare values are used here.
        return new JobParametersBuilder()
            .addLong(JobParameterKeys.START_TIME, System.currentTimeMillis())
            .addLong(JobParameterKeys.BUCKET_ID, bucket.getId())
            .addString(JobParameterKeys.BUCKET_DIRECTORY, bucket.getDirectory())
            .toJobParameters();
    }

    @Override
    public boolean stop(final long executionId)  {
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
        scans.values().forEach(status -> {
            status.setExitStatus(getExitStatus(status.getId()));
            status.setBatchStatus(getBatchStatus(status.getId()));
        });
        return Collections.unmodifiableList(new ArrayList<>(scans.values()));
    }

    @Override
    public ExitStatus getExitStatus(long id) {
        LOGGER.debug("Get exit status for job execution with id: " + id + ".");
        final JobExecution jobExecution = explorer.getJobExecution(id);

        if (jobExecution != null) {
            return jobExecution.getExitStatus();
        } else {
            throw new ScanError("JobExecution with id %d not found!", id);
        }

    }

    @Override
    public BatchStatus getBatchStatus(final long id)  {
        LOGGER.debug("Get batch status for job execution with id: " + id + ".");
        final JobExecution jobExecution = explorer.getJobExecution(id);

        if (jobExecution != null) {
            return jobExecution.getStatus();
        } else {
            throw new ScanError("JobExecution with id %d not found!", id);
        }
    }
}
