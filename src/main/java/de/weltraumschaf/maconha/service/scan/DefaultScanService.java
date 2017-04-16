package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.service.ScanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private final JobRegistry registry;
    private final JobLauncher launcher;
    private final JobOperator operator;
    private final JobExplorer explorer;
    private final JobRepository repo;

    @Autowired
    DefaultScanService(final JobRegistry registry, final JobLauncher launcher, final JobOperator operator, final JobExplorer explorer, final JobRepository repo) {
        super();
        this.registry = registry;
        this.launcher = launcher;
        this.operator = operator;
        this.explorer = explorer;
        this.repo = repo;
    }

    @Override
    public Long scan(final Bucket bucket) throws ScanError {
        Validate.notNull(bucket, "bucket");
        LOGGER.debug("Scan bucket with id {} and directory {} ...", bucket.getId(), bucket.getDirectory());

        try {
            final Job job = registry.getJob(JOB_NAME);
            final JobParameters parameters = job.getJobParametersIncrementer().getNext(createJobParameters(bucket));
            return launcher.run(job, parameters).getId();
        } catch (final NoSuchJobException e) {
            throw new ScanError(e, "There is no such job with name '%s'!", JOB_NAME);
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
        return new JobParametersBuilder()
            .addLong(JobParameterKeys.BUCKET_ID, bucket.getId())
            .addString(JobParameterKeys.BUCKET_DIRECTORY, bucket.getDirectory())
            .toJobParameters();
    }

    @Override
    public boolean stop(final long executionId) throws ScanError {
        LOGGER.debug("Stop JobExecution with id: " + executionId);
        try {
            return operator.stop(executionId);
        } catch (final NoSuchJobExecutionException e) {
            throw new ScanError(e, "No job execution present for id %d!", executionId);
        } catch (final JobExecutionNotRunningException e) {
            throw new ScanError(e, "Jo execution for id %d not running!", executionId);
        }
    }

    @Override
    public String getStatus(final long executionId) throws ScanError {
        LOGGER.debug("Get ExitCode for JobExecution with id: " + executionId + ".");
        final JobExecution jobExecution = explorer.getJobExecution(executionId);

        if (jobExecution != null) {
            return jobExecution.getExitStatus().getExitCode();
        } else {
            throw new ScanError("JobExecution with id %d not found!", executionId);
        }
    }
}