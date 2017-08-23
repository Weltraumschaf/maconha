package de.weltraumschaf.maconha.backend.service.scan.reporting;

import de.weltraumschaf.commons.validate.Validate;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Immutable holder of event loop report.
 */
public final class Report {
    private final Collection<ReportEntry> entries;

    /**
     * Dedicated constructor.
     *
     * @param entries must not be {@code null}, defensive copy because the origin may be changed.
     */
    Report(final Collection<ReportEntry> entries) {
        super();
        this.entries = Validate.notNull(new ArrayList<>(entries), "entries");
    }
}
