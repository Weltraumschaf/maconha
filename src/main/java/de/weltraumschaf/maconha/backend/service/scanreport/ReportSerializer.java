package de.weltraumschaf.maconha.backend.service.scanreport;

import de.weltraumschaf.maconha.backend.service.ScanService.ScanStatus;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.Report;

import java.io.Reader;
import java.util.Collection;

/**
 * Helper to de-/serialize reports.
 */
interface ReportSerializer {
    /**
     * Serializes the given report to a given writer.
     *
     * @param report must not be {@code null}
     * @param writer   must not be {@code null}
     */
    void serialize(final Report report, final Appendable writer);

    /**
     * Deserialize report from given reader.
     *
     * @param reader must not be {@code null}
     * @return never {@code null}
     */
    Report deserialize(final Reader reader);
}
