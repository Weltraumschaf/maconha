package de.weltraumschaf.maconha.backend.service.scan;

/**
 * Background task to scan a bucket.
 */
interface ScanTask extends Runnable {
    /**
     * Stops the task.
     */
    void stop();

    /**
     * Get the unique id of the task.
     *
     * @return greater 0
     */
    Long getId();

    /**
     * Get the current status.
     *
     * @return never {@code null}
     */
    ScanStatus getStatus();

    /**
     * The status of a scan.
     */
    enum ScanStatus {
        /**
         * Newly created task.
         */
        CREATED,
        /**
         * Running task.
         */
        RUNNING,
        /**
         * Stopping task.
         */
        STOPPING,
        /**
         * Sopped task.
         */
        STOPPED,
        /**
         * Aborted by error task.
         */
        ABORTED,
        /**
         * Normally finished task.
         */
        COMPLETED;
    }
}
