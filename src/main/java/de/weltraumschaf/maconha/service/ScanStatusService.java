package de.weltraumschaf.maconha.service;

import java.util.Collection;

/**
 * Service to read and store the scan statuses persistent.
 */
public interface ScanStatusService {

    /**
     * Get all stored statuses.
     * <p>
     * This method fails silently if the data can not be read for any reason, but logs an error message.
     * </p>
     *
     * @return never {@code null}, may be empty, unmodifiable
     */
    Collection<ScanService.ScanStatus> allStatuses();

    /**
     * Stores a single status persistent.
     * <p>
     * This method fails silently if the data can not be stored for any reason, but logs an error message.
     * </p>
     *
     * @param status must not be {@code null}
     */
    void storeStatus(ScanService.ScanStatus status);

    /**
     * Returns the next usable status ID.
     *
     * @return greater 0
     */
    long nextId();
}
