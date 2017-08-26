package de.weltraumschaf.maconha.backend.service.scanreport.reporting;

import de.weltraumschaf.commons.validate.Validate;

import java.util.Objects;

/**
 * A single immutable report entry.
 */
public final class ReportEntry {
    private final ReportEntryType type;
    private final String source;
    private final String message;

    /**
     * Dedicated constructor.
     * <p>
     * The format message has the same options like {@link String#format(String, Object...) format string}.
     * </p>
     *
     * @param type must not be {@code null}
     * @param source must not be {@code null} or empty
     * @param formatMessage   must not be {@code null} or empty
     * @param formatArguments optional format arguments
     */
    ReportEntry(final ReportEntryType type, final String source, final String formatMessage, final Object... formatArguments) {
        super();
        this.type = Validate.notNull(type, "type");
        this.source = Validate.notEmpty(source, "source");
        this.message = String.format(Validate.notEmpty(formatMessage, "formatMessage"), formatArguments);
    }

    /**
     * Type of entry.
     *
     * @return never {@code null}
     */
    public ReportEntryType getType() {
        return type;
    }

    /**
     * Type of object which made the entry.
     *
     * @return never {@code null}
     */
    public String getSource() {
        return source;
    }

    /**
     * Get the message of the entry.
     *
     * @return never {@code null} or empty
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ReportEntry{" +
            "type=" + type +
            ", source=" + source +
            ", message='" + message + '\'' +
            '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof ReportEntry)) {
            return false;
        }

        final ReportEntry that = (ReportEntry) o;
        return type == that.type &&
            Objects.equals(source, that.source) &&
            Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, source, message);
    }
}
