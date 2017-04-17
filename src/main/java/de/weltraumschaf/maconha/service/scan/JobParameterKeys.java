package de.weltraumschaf.maconha.service.scan;

/**
 * Provides keys used to store and retrieve job parameters.
 */
final class JobParameterKeys {

    /**
     * Key for start time in milliseconds since epoch.
     */
    static final String START_TIME = "startTime";
    /**
     * The id of the bucket to scan.
     */
    static final String BUCKET_ID = "bucket.id";
    /**
     * The directory of the bucket to scan.
     */
    static final String BUCKET_DIRECTORY = "bucket.directory";

    /**
     * This class only provides string constants.
     */
    private JobParameterKeys() {
        super();
    }
}
