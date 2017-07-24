package de.weltraumschaf.maconha.backend.service;

import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;

import java.util.Collections;
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
     * @param currentUi must not be {@code null}
     * @throws ScanError if the scan fails for any reason
     */
    void scan(Bucket bucket, UI currentUi);

    boolean stop(long executionId);

    List<ScanStatus> overview();

    /**
     * Immutable data structure to describe the current status of a job.
     */
    final class ScanStatus {
        private final Long id;
        private final String bucketName;
        private final String creationTime;
        private final String startTime;
        private final String endTime;
        private final String duration;
        private final String jobStatus;
        private final String jobExitCode;
        private final List<Throwable> allFailureExceptions;
        private final String type;

        public ScanStatus(final Long id, final String bucketName, final String creationTime, final String startTime, final String endTime, final String duration, final String jobStatus, final String type) {
            this(id,bucketName, creationTime, startTime, endTime, duration, jobStatus, jobStatus, Collections.emptyList(), type);
        }

        public ScanStatus(final Long id, final String bucketName, final String creationTime, final String startTime, final String endTime, final String duration, final String jobStatus, final String jobExitCode, final List<Throwable> allFailureExceptions, final String type) {
            super();
            this.id = id;
            this.bucketName = bucketName;
            this.creationTime = creationTime;
            this.startTime = startTime;
            this.endTime = endTime;
            this.duration = duration;
            this.jobStatus = jobStatus;
            this.jobExitCode = jobExitCode;
            this.allFailureExceptions = allFailureExceptions;
            this.type = type;
        }

        public Long getId() {
            return id;
        }

        public String getBucketName() {
            return bucketName;
        }

        public String getCreationTime() {
            return creationTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public String getDuration() {
            return duration;
        }

        public String getJobStatus() {
            return jobStatus;
        }

        public String getJobExitCode() {
            return jobExitCode;
        }

        public List<Throwable> getAllFailureExceptions() {
            return allFailureExceptions;
        }

        public String getType() {
            return type;
        }

        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof ScanStatus)) {
                return false;
            }

            final ScanStatus status = (ScanStatus) o;
            return Objects.equals(id, status.id) &&
                Objects.equals(bucketName, status.bucketName) &&
                Objects.equals(creationTime, status.creationTime) &&
                Objects.equals(startTime, status.startTime) &&
                Objects.equals(endTime, status.endTime) &&
                Objects.equals(duration, status.duration) &&
                Objects.equals(jobStatus, status.jobStatus) &&
                Objects.equals(jobExitCode, status.jobExitCode) &&
                Objects.equals(allFailureExceptions, status.allFailureExceptions) &&
                Objects.equals(type, status.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, bucketName, creationTime, startTime, endTime, duration, jobStatus, jobExitCode, allFailureExceptions, type);
        }

        @Override
        public String toString() {
            return "ScanStatus{" +
                "id=" + id +
                ", bucketName='" + bucketName + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", duration='" + duration + '\'' +
                ", jobStatus='" + jobStatus + '\'' +
                ", jobExitCode='" + jobExitCode + '\'' +
                ", allFailureExceptions=" + allFailureExceptions +
                ", type='" + type + '\'' +
                '}';
        }
    }

    /**
     * Thrown if any error occurs during a scan related task.
     */
    final class ScanError extends RuntimeException {
        /**
         * Convenience constructor to change any throwable into a {@link ScanError}.
         *
         * @param t must not be {@code null}
         */
        public ScanError(final Throwable t) {
            this(t, t.getMessage());
        }

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