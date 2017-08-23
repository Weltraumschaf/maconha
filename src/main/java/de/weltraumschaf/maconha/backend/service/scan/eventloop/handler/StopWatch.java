package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

/**
 * Helper class to measure time.
 */
final class StopWatch {
    private static final long UNINITIALISED = -1L;
    private long startTime = UNINITIALISED;
    private long stopTime = UNINITIALISED;

    void start() {
        if (startTime > UNINITIALISED) {
            throw new IllegalStateException("Stopwatch already started!");
        }

        startTime = System.currentTimeMillis();
    }

    void stop() {
        if (stopTime > UNINITIALISED) {
            throw new IllegalStateException("Stopwatch already stopped!");
        }

        stopTime = System.currentTimeMillis();
    }

    long duration() {
        if (stopTime <= UNINITIALISED) {
            throw new IllegalStateException("Stop watch never started! Start first.");
        }

        if (stopTime <= UNINITIALISED) {
            throw new IllegalStateException("Stop watch never stopped! Stop first.");
        }

        return stopTime - startTime;
    }
}
