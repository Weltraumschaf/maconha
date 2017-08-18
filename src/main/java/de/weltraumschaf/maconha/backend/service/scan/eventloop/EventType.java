package de.weltraumschaf.maconha.backend.service.scan.eventloop;

/**
 * Type of events.
 */
public enum EventType {
    /**
     * Hash the files in a bucket.
     */
    DIR_HASH,
    /**
     * Load the cheksum files with hashes from a bucket.
     */
    LOAD_FILE_HASHES,
    /**
     * Split the checksum file content into lines.
     */
    SPLIT_CHECKSUM_LINES,
    /**
     * Parse a checksum line.
     */
    PARSE_CHECKSUM_LINE,
    /**
     * Filter ouyt files with unwanted file extension.
     */
    FILTER_FILE_EXTENSION,
    /**
     * Make the absolute path of a hashed file relative to a bucket.
     */
    RELATIVIZE_HASHED_FILE,
    /**
     * Filter out already seen files.
     */
    FILTER_SEEN_HASHED_FILE,
    /**
     * Extract the meta data from a file.
     */
    EXTRACT_FILE_META_DATA,
    /**
     * Extract keywords from file name.
     */
    EXTRACT_KEYWORDS_FROM_FILE_NAME,
    /**
     * Extract keywords from meta data.
     */
    EXTRACT_KEYWORDS_FROM_META_DATA,
    /**
     * Filter out unwanted keywords.
     */
    FILTER_UNWANTED_KEYWORDS,
    /**
     * Store media file and keywords persistent.
     */
    STORE_FILE_AND_KEYWORDS;
}