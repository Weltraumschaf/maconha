package de.weltraumschaf.maconha.backend.service.scan;

/**
 * Background task to scan a bucket.
 */
interface ScanTask extends Runnable {
    /**
     * Stops the task.
     */
    void stop();
}
