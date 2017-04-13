package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Bucket;

/**
 * This service provides the business logic to deal with {@link Bucket buckets}.
 */
public interface ScanService {
    void scan(Bucket bucket);
}
