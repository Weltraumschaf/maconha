package de.weltraumschaf.maconha.service.scan;

import org.springframework.batch.core.JobExecution;

/**
 * Clients which want to be called back must implement this interface.
 */
public interface ScanCallBack {
    /**
     * Will be invoked before a job starts.
     *
     * @param id the unique id of the scan
     */
    void beforeScan(long id);

    /**
     * Will be invoked after a job starts.
     *
     * @param id the unique id of the scan
     */
    void afterScan(long id);
}
