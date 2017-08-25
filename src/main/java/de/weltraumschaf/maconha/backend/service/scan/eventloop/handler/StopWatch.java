package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

/**
 * Helper class to measure time.
 */
final class StopWatch {
    private static final long UNINITIALISED = -1L;
    private long startTime = UNINITIALISED;
    private long stopTime = UNINITIALISED;

    /**
     * Indicates if the stopwatch was {@link #start() started}.
     *
     * @return {@code true} if started, else {@code false}
     */
    boolean isStarted() {
        return startTime > UNINITIALISED;
    }

    /**
     * Indicates if the stopwatch was {@link #stop() stopped}.
     *
     * @return {@code true} if stopped, else {@code false}
     */
    boolean isStopped() {
        return stopTime > UNINITIALISED;
    }

    /**
     * Starts the stopwatch.
     * <p>
     * Throws an {@link IllegalStateException}, if the stopwatch was already stared before.
     * </p>
     */
    void start() {
        if (isStarted()) {
            throw new IllegalStateException("Stopwatch already started!");
        }

        startTime = System.currentTimeMillis();
    }

    /**
     * Stopps the stopwatch.
     * <p>
     * Throws an {@link IllegalStateException}, if the stopwatch was already stopped before or never
     * started.
     * </p>
     */

    void stop() {
        if (!isStarted()) {
            throw new IllegalStateException("Stopwatch never started! Start it first.");
        }

        if (isStopped()) {
            throw new IllegalStateException("Stopwatch already stopped!");
        }

        stopTime = System.currentTimeMillis();
    }

    /**
     * Get the measured time in milliseconds.
     * <p>
     * Throws {@link IllegalStateException} if not {@link #start() started} or {@link #stop() stopped}
     * before.
     * </p>
     * <p>
     * You can call this method as oftne as you want, it will always return the same value.
     * </p>
     *
     * @return greater 0
     */
    long duration() {
        if (!isStarted()) {
            throw new IllegalStateException("Stop watch never started! Start first.");
        }

        if (!isStopped()) {
            throw new IllegalStateException("Stop watch never stopped! Stop first.");
        }

        return stopTime - startTime;
    }

    /**
     * Resets the stopwatch into the same state after creating a new one.
     * <p>
     * This method is useful to reuse a stopwatch. After calling this method you can call
     * {@link #start()} and {@link #stop()} again.
     * </p>
     */
    void reset() {
        startTime = UNINITIALISED;
        stopTime = UNINITIALISED;
    }
}
