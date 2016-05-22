package de.weltraumschaf.maconha.job;

import java.util.concurrent.Callable;

/**
 * @param <V>
 */
public interface Job<V> extends Callable<V>, MessageProducer {

    Description describe();

    void cancel();

    boolean isCanceled();

    boolean isRunning();

    boolean isFinished();

    State getState();

    enum State {

        NEW, CANCELED, RUNNING, FINISHED;
    }
}
