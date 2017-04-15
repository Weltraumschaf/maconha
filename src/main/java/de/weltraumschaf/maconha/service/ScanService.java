package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Bucket;

/**
 * This service provides the business logic to deal with {@link Bucket buckets}.
 */
public interface ScanService {
    String JOB_NAME = "ScanJob";

    Long scan(Bucket bucket) throws ScanError;

    boolean stop(long executionId) throws ScanError;

    String getStatus(long executionId) throws ScanError;

    class ScanError extends Exception {
        public ScanError(final String message, Object... args) {
            this(null, message, args);
        }

        public ScanError(final Throwable cause, final String message, Object... args) {
            super(String.format(message, args), cause);
        }
    }
}
