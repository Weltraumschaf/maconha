package de.weltraumschaf.maconha.service.scan.batch;

/**
 * Provides keys used to store and retrieve job parameters.
 */
public final class JobParameterKeys {

    /**
     * Key for start time in milliseconds since epoch.
     */
    public static final String START_TIME = "startTime";
    /**
     * The id of the bucket to scan.
     */
    public static final String BUCKET_ID = "bucket.id";
    /**
     * The directory of the bucket to scan.
     */
    public static final String BUCKET_DIRECTORY = "bucket.directory";

    /**
     * This class only provides string constants.
     */
    private JobParameterKeys() {
        super();
    }
}
