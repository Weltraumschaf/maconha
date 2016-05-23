package de.weltraumschaf.maconha.job;

import java.util.concurrent.Callable;

/**
 * Jobs are long running tasks executed in the background.
 *
 * @param <V> produced type of job
 */
public interface Job<V> extends Callable<V>, MessageProducer {

    JobDescription describe();

    void cancel();

    boolean isCanceled();

    boolean isRunning();

    boolean isFinished();

    State getState();

    /**
     * State of a job.
     */
    enum State {

        NEW, CANCELED, RUNNING, FINISHED;
    }
}
