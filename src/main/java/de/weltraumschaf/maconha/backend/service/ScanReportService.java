package de.weltraumschaf.maconha.backend.service;

import de.weltraumschaf.maconha.backend.service.ScanService.ScanStatus;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.Report;

/**
 * Service to read and store the scan reports persistent.
 */
public interface ScanReportService {
    /**
     * Stores a report for a status.
     *
     * @param status must not be {@code null}
     * @param report must not be {@code null}
     */
    void store(ScanStatus status, Report report);

    /**
     * Loads a report for a status.
     *
     * @param status must not be {@code null}
     * @return never {@code null}, {@link Report#EMPTY} if no report was found
     */
    Report load(ScanStatus status);

    /**
     * Deletes the report for given status.
     *
     * @param status must not be {@code null}
     */
    void delete(ScanStatus status);

    /**
     * Deletes all reports.
     */
    void deleteAll();
}
