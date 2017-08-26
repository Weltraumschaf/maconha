package de.weltraumschaf.maconha.backend.service.scan;

import de.weltraumschaf.maconha.backend.service.scanreport.reporting.Report;

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
    ScanTaskStatus getStatus();

    /**
     * Get the current report.
     * <p>
     * The report may have more more entries until the task ist {@link #stop() stopped}.
     * </p>
     *
     * @return never {@code null}, maybe {@link Report#EMPTY}
     */
    Report getReport();

    /**
     * The status of a scan.
     */
    enum ScanTaskStatus {
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
