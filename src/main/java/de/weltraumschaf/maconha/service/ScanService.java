package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Bucket;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.batch.core.BatchStatus;
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

    ExitStatus getExitStatus(long id);
    BatchStatus getBatchStatus(long id);

    final class ScanStatus {
        private final Long id;
        private final DateTime startTime = DateTime.now();
        private ExitStatus exitStatus = ExitStatus.UNKNOWN;
        private BatchStatus batchStatus = BatchStatus.UNKNOWN;

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
                    .printZeroAlways()
                    .minimumPrintedDigits(2)
                    .appendHours()
                    .appendSeparator(":")
                    .appendMinutes()
                    .appendSeparator(":")
                    .appendSeconds()
                    .toFormatter();
            // FIXME Do not calculate if stopped.
            return format.print(Seconds.secondsBetween(startTime, DateTime.now()));
        }

        public String getExitStatus() {
            return exitStatus.getExitCode();
        }

        public void setExitStatus(final ExitStatus exitStatus) {
            this.exitStatus = exitStatus;
        }

        public String getBatchStatus() {
            return batchStatus.toString();
        }

        public void setBatchStatus(final BatchStatus batchStatus) {
            this.batchStatus = batchStatus;
        }

        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof ScanStatus)) {
                return false;
            }

            final ScanStatus other = (ScanStatus) o;
            return Objects.equals(id, other.id) &&
                Objects.equals(startTime, other.startTime) &&
                Objects.equals(exitStatus, other.exitStatus) &&
                Objects.equals(batchStatus, other.batchStatus);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, startTime, exitStatus, batchStatus);
        }

        @Override
        public String toString() {
            return "ScanStatus{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", exitStatus=" + exitStatus +
                ", batchStatus=" + batchStatus +
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
