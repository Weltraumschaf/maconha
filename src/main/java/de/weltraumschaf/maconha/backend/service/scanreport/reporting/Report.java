package de.weltraumschaf.maconha.backend.service.scanreport.reporting;

import de.weltraumschaf.commons.validate.Validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Immutable holder of event loop report.
 */
public final class Report {
    /**
     * Empty report used instead of {@code null}.
     */
    public static final Report EMPTY = new Report(Collections.emptyList());
    private final List<ReportEntry> entries;

    /**
     * Dedicated constructor.
     *
     * @param entries must not be {@code null}, defensive copy because the origin may be changed.
     */
    Report(final Collection<ReportEntry> entries) {
        super();
        this.entries = Validate.notNull(new ArrayList<>(entries), "entries");
    }

    /**
     * Get the report entries.
     *
     * @return never {@code null}, unmodifiable
     */
    public List<ReportEntry> entries() {
        return Collections.unmodifiableList(entries);
    }
}
