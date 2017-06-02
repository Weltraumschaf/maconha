package de.weltraumschaf.maconha.service.scan;

import com.github.rjeschke.txtmark.Run;
import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.model.Bucket;
import org.joda.time.DateTime;

import java.util.Objects;

/**
 * Data structure to remember started scan jobs.
 */
final class Execution {
    static final Runnable NO_THREAD = new Runnable() {
        @Override
        public void run() {

        }
    };
    private final Long id;
    private final Bucket bucket;
    private final UI currentUi;
    private final Runnable thread;
    private DateTime startTime;
    private DateTime stopTime;

    Execution(final Long id, final Bucket bucket, final UI currentUi) {
        this(id, bucket, currentUi, NO_THREAD);
    }

    Execution(final Long id, final Bucket bucket, final UI currentUi, final Runnable thread) {
        super();
        this.id = id;
        this.bucket = bucket;
        this.currentUi = currentUi;
        this.thread = thread;
    }

    Long getId() {
        return id;
    }

    Bucket getBucket() {
        return bucket;
    }

    UI getCurrentUi() {
        return currentUi;
    }

    void start() {
        if (null != startTime) {
            throw new IllegalStateException("Already started! The method start() should be called only once.");
        }

        startTime = DateTime.now();
    }

    void stop() {
        if (null != stopTime) {
            throw new IllegalStateException("Already stopped! The method stop() should be called only once.");
        }

        stopTime = DateTime.now();
    }

    Runnable getThread() {
        return thread;
    }

    DateTime getStartTime() {
        if (null == startTime) {
            throw new IllegalStateException("Not started yet! The method start() must be called first.");
        }

        return startTime;
    }

    DateTime getStopTime() {
        if (null == startTime) {
            throw new IllegalStateException("Not stopped yet! The method stop() must be called first.");
        }

        return stopTime;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Execution)) {
            return false;
        }

        final Execution execution = (Execution) o;
        return Objects.equals(id, execution.id) &&
            Objects.equals(bucket, execution.bucket) &&
            Objects.equals(currentUi, execution.currentUi) &&
            Objects.equals(thread, execution.thread) &&
            Objects.equals(startTime, execution.startTime) &&
            Objects.equals(stopTime, execution.stopTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bucket, currentUi, thread, startTime, stopTime);
    }

    @Override
    public String toString() {
        return "Execution{" +
            "id=" + id +
            ", bucket=" + bucket +
            ", currentUi=" + currentUi +
            ", thread=" + thread +
            ", startTime=" + startTime +
            ", stopTime=" + stopTime +
            '}';
    }
}
