package de.weltraumschaf.maconha.backend.service.scan.reporting;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.HasLogger;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Collects entries in a thread safe way for a report.
 */
public final class Reporter implements HasLogger {
    private final Collection<ReportEntry> entries = new CopyOnWriteArrayList<>();

    /**
     * Creates a normal entry in the report.
     * <p>
     * The format message has the same options like {@link String#format(String, Object...) format string}.
     * </p>
     *
     * @param source          must not be {@code null}
     * @param formatMessage   must not be {@code null} or empty
     * @param formatArguments optional format arguments
     */
    public void normal(final Class<?> source, final String formatMessage, final Object... formatArguments) {
        Validate.notEmpty(formatMessage, "formatMessage");
        createAndAdd(ReportEntryType.NORMAL, source, formatMessage, formatArguments);
    }

    /**
     * Creates a error entry in the report.
     * <p>
     * The format message has the same options like {@link String#format(String, Object...) format string}.
     * </p>
     *
     * @param source          must not be {@code null}
     * @param formatMessage   must not be {@code null} or empty
     * @param formatArguments optional format arguments
     */
    public void error(final Class<?> source, final String formatMessage, final Object... formatArguments) {
        Validate.notEmpty(formatMessage, "formatMessage");
        createAndAdd(ReportEntryType.ERROR, source, formatMessage, formatArguments);
    }

    private void createAndAdd(final ReportEntryType type, final Class<?> source, final String formatMessage, final Object... formatArguments) {
        final ReportEntry entry = new ReportEntry(type, source, formatMessage, formatArguments);

        if (type == ReportEntryType.ERROR) {
            logger().warn(entry.getMessage());
        } else {
            logger().debug(entry.getMessage());
        }

        entries.add(entry);
    }

    /**
     * Get the current report.
     * <p>
     * The returned report only contains entires added until this method is called.
     * </p>
     *
     * @return never {@code null}
     */
    public Report getReport() {
        return new Report(entries);
    }
}
