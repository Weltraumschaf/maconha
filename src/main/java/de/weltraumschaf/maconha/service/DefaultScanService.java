package de.weltraumschaf.maconha.service;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.model.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
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

    @Autowired
    private JobRegistry registry;
    @Autowired
    private JobLauncher launcher;
    @Autowired
    private JobOperator operator;
    @Autowired
    private JobExplorer explorer;

    @Override
    public Long scan(final Bucket bucket) throws ScanError {
        Validate.notNull(bucket, "bucket");
        LOGGER.debug("Scan bucket with id {} and directory {} ...", bucket.getId(), bucket.getDirectory());

        try {
            final Job job = registry.getJob(JOB_NAME);
            final JobParameters parameters = new JobParameters();
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
