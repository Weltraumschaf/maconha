package de.weltraumschaf.maconha.backend.service.scan.eventloop;

/**
 *
 */
public enum EventType {
    DIR_HASH,
    LOAD_FILE_HASHES,
    SPLIT_CHECKSUM_LINES,
    PARSE_CHECKSUM_LINE,
    RELATIVIZE_FILE,
    FILTER_SEEN_FILE;
}
