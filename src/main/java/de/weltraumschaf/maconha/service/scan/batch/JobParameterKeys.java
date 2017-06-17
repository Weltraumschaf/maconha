package de.weltraumschaf.maconha.service.scan.batch;

/**
 * Provides keys used to store and retrieve job parameters.
 */
public interface JobParameterKeys {

    /**
     * Key for start time in milliseconds since epoch.
     */
    String START_TIME = "startTime";
    /**
     * The id of the bucket to scan.
     */
    String BUCKET_ID = "bucket.id";
    /**
     * The directory of the bucket to scan.
     */
    String BUCKET_DIRECTORY = "bucket.directory";

}
