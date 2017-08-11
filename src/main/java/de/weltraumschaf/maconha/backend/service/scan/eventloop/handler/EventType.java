package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

/**
 *
 */
interface EventType {
    String DIR_HASH = "DIR_HASH";
    String LOAD_FILE_HASHES = "LOAD_FILE_HASHES";
    String SPLIT_CHECKSUM_LINES = "SPLIT_CHECKSUM_LINES";
    String PARSE_CHECKSUM_LINE = "PARSE_CHECKSUM_LINE";
    String RELATIVIZE_FILE = "RELATIVIZE_FILE";
    String FILTER_SEEN_FILE = "FILTER_SEEN_FILE";
}
