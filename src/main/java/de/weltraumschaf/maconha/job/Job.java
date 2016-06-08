package de.weltraumschaf.maconha.job;

import java.util.concurrent.Callable;

/**
 * Jobs are long running tasks executed in the background.
 *
 * @param <V> produced type of job
 */
public interface Job<V> extends Callable<V>, MessageProducer, Configurable {

    /**
     * Gives current information about a job.
     *
     * @return never {@code null} always new instance
     */
    JobInfo info();

    /**
     * Cancel the job.
     * <p>
     * Implementation is responsible to deal with this request and stop working.
     * </p>
     */
    void cancel();

    /**
     * Whether the job is canceled.
     *
     * @return {@code true} if canceled, unless {@code false}
     */
    boolean isCanceled();

    /**
     * Whether the job is running.
     *
     * @return {@code true} if running, unless {@code false}
     */
    boolean isRunning();

    /**
     * Whether the job is finished.
     *
     * @return {@code true} if finished, unless {@code false}
     */
    boolean isFinished();

    /**
     * Get the state in which the job is.
     * <p>
     * By default the state is {@link State#NEW} until the job starts executing.
     * </p>
     *
     * @return never {@code null}
     */
    State getState();

    /**
     * State of a job.
     */
    enum State {
        /**
         * Created, but not started.
         */
        NEW,
        /**
         * Should stop doing work.
         */
        CANCELED,
        /**
         * Does his work.
         */
        RUNNING,
        /**
         * Job is done.
         */
        FINISHED,
        /**
         * Job which had some errors.
         */
        FAILED;
    }
}
