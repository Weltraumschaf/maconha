package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Bucket;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.batch.core.ExitStatus;

import java.util.List;
import java.util.Objects;

/**
 * This service provides the business logic to deal with {@link Bucket buckets}.
 */
public interface ScanService {
    String JOB_NAME = "ScanJob";

    /**
     * Scan the media files found in the given bucket
     *
     * @param bucket must not be {@code null}
     * @return the id of the underling scan job
     * @throws ScanError if the scan fails for any reason
     */
    Long scan(Bucket bucket);

    boolean stop(long executionId);

    List<ScanStatus> overview();

    ExitStatus getStatus(long id);

    final class ScanStatus {
        private final Long id;
        private final DateTime startTime = DateTime.now();
        private ExitStatus status = ExitStatus.UNKNOWN;

        public ScanStatus(final Long id) {
            super();
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public String getStartTime() {
            final DateTimeFormatter format = DateTimeFormat.forPattern("HH:mm:ss MM.dd.yyyy");
            return format.print(startTime);
        }

        public String getElapsedTime() {
            final PeriodFormatter format =
                new PeriodFormatterBuilder()
                    .printZeroAlways().minimumPrintedDigits(2).appendHours()
                    .appendSeparator(":")
                    .printZeroAlways().minimumPrintedDigits(2).appendMinutes()
                    .appendSeparator(":")
                    .printZeroAlways().minimumPrintedDigits(2).appendSeconds()
                    .toFormatter();

            return format.print(Seconds.secondsBetween(startTime, DateTime.now()));
        }

        public String getStatusCode() {
            return status.getExitCode();
        }

        public String getExitDescription() {
            return status.getExitDescription();
        }

        public void setStatus(final ExitStatus status) {
            this.status = status;
        }

        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof ScanStatus)) {
                return false;
            }

            final ScanStatus status1 = (ScanStatus) o;
            return Objects.equals(id, status1.id) &&
                Objects.equals(startTime, status1.startTime) &&
                Objects.equals(status, status1.status);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, startTime, status);
        }

        @Override
        public String toString() {
            return "ScanStatus{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", status=" + status +
                '}';
        }
    }

    /**
     * Thrown if any error occurs during a scan related task.
     */
    final class ScanError extends RuntimeException {
        /**
         * Convenience constructor w/o cause.
         *
         * @param message format string, must not be {@code null}
         * @param args    optional arguments for format string
         */
        public ScanError(final String message, Object... args) {
            this(null, message, args);
        }

        /**
         * Dedicated constructor.
         *
         * @param cause   may be {@code null}
         * @param message format string, must not be {@code null}
         * @param args    optional arguments for format string
         */
        public ScanError(final Throwable cause, final String message, Object... args) {
            super(String.format(message, args), cause);
        }
    }
}
