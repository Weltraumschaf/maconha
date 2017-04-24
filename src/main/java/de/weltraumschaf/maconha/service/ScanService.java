package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Bucket;

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
    Long scan(Bucket bucket) throws ScanError;

    boolean stop(long executionId) throws ScanError;

    String getStatus(long executionId) throws ScanError;

    /**
     * Thrown if any error occurs during a scan related task.
     */
    class ScanError extends Exception {
        /**
         * Convenience constructor w/o cause.
         *
         * @param message format string, must not be {@code null}
         * @param args optional arguments for format string
         */
        public ScanError(final String message, Object... args) {
            this(null, message, args);
        }

        /**
         * Dedicated constructor.
         *
         * @param cause may be {@code null}
         * @param message format string, must not be {@code null}
         * @param args optional arguments for format string
         */
        public ScanError(final Throwable cause, final String message, Object... args) {
            super(String.format(message, args), cause);
        }
    }
}
