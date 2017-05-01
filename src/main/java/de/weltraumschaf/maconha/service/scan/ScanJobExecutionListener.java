package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.commons.validate.Validate;
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
final class ScanJobExecutionListener implements JobExecutionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanJobExecutionListener.class);
    private final Collection<CallBack> callbacks = new CopyOnWriteArrayList<>();

    @Override
    public void beforeJob(final JobExecution jobExecution) {
        LOGGER.debug("Scan job execution listener called before job with id {}.", jobExecution.getJobId());
        callbacks.forEach(callback -> callback.beforeJob(jobExecution));
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        LOGGER.debug("Scan job execution listener called after job with id {}.", jobExecution.getJobId());
        callbacks.forEach(callBack -> callBack.afterJob(jobExecution));
    }

    /**
     * Registers a call back to be notified before and after job execution.
     *
     * @param callback must not be {@code null}
     */
    void register(final CallBack callback) {
        Validate.notNull(callback, "callback");
        callbacks.add(callback);
    }

    /**
     * Clients which want to be called back must implement this interface.
     */
    interface CallBack {
        /**
         * Will be invoked before a job starts.
         *
         * @param jobExecution must not be {@code null}
         */
        void beforeJob(final JobExecution jobExecution);

        /**
         * Will be invoked after a job starts.
         *
         * @param jobExecution must not be {@code null}
         */
        void afterJob(final JobExecution jobExecution);
    }
}
