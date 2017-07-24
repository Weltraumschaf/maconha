package de.weltraumschaf.maconha.backend.service.scan.batch;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.service.scan.ScanCallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This lister calls all registered call backs before and after a job.
 */
@Component
public final class ScanJobExecutionListener implements JobExecutionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanJobExecutionListener.class);
    private final Collection<ScanCallBack> callbacks = new CopyOnWriteArrayList<>();

    @Override
    public void beforeJob(final JobExecution jobExecution) {
        LOGGER.debug("Scan job execution listener called before job with id {}.", jobExecution.getJobId());
        callbacks.forEach(callback -> callback.beforeScan(jobExecution.getJobId()));
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        LOGGER.debug("Scan job execution listener called after job with id {}.", jobExecution.getJobId());
        callbacks.forEach(callBack -> callBack.afterScan(jobExecution.getJobId()));
    }

    /**
     * Registers a call back to be notified before and after job execution.
     *
     * @param callback must not be {@code null}
     */
    public void register(final ScanCallBack callback) {
        Validate.notNull(callback, "callback");
        callbacks.add(callback);
    }

}