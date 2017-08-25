package de.weltraumschaf.maconha.backend.service.scan;

import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import org.joda.time.DateTime;

/**
 * Data structure to remember started scan jobs.
 */
final class Execution {
    private final Bucket bucket;
    private final UI currentUi;
    private final ScanTask task;
    private final DateTime creationTime;
    private DateTime startTime;
    private DateTime stopTime;

    Execution(final Bucket bucket, final UI currentUi, final ScanTask task) {
        super();
        this.bucket = bucket;
        this.currentUi = currentUi;
        this.task = task;
        this.creationTime = DateTime.now();
    }

    Bucket getBucket() {
        return bucket;
    }

    UI getCurrentUi() {
        return currentUi;
    }

    ScanTask getTask() {
        return task;
    }

    DateTime getCreationTime() {
        return creationTime;
    }

    void start() {
        if (hasStartTime()) {
            throw new IllegalStateException("Already started! The method start() should be called only once.");
        }

        startTime = DateTime.now();
    }

    DateTime getStartTime() {
        if (hasStartTime()) {
            return startTime;
        }

        throw new IllegalStateException("Not started yet! The method start() must be called first.");
    }

    boolean hasStartTime() {
        return null != startTime;
    }

    void stop() {
        if (hasStopTime()) {
            throw new IllegalStateException("Already stopped! The method stop() should be called only once.");
        }

        stopTime = DateTime.now();
    }

    DateTime getStopTime() {
        if (hasStopTime()) {
            return stopTime;
        }

        throw new IllegalStateException("Not stopped yet! The method stop() must be called first.");
    }

    boolean hasStopTime() {
        return null != stopTime;
    }

    @Override
    public String toString() {
        return "Execution{" +
            "bucket=" + bucket +
            ", currentUi=" + currentUi +
            ", task=" + task +
            ", creationTime=" + creationTime +
            ", startTime=" + startTime +
            ", stopTime=" + stopTime +
            '}';
    }
}
