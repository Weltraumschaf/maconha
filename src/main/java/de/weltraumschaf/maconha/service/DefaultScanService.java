package de.weltraumschaf.maconha.service;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.repo.BucketRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation.
 * <p>
 * This class is package private because it must not be used directly from outside. Use the DI of Spring to get an
 * instance.
 * </p>
 */
@Service
@Transactional
final class DefaultScanService implements ScanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultScanService.class);

    @Override
    public void scan(final Bucket bucket) {
        Validate.notNull(bucket, "bucket");
        LOGGER.debug("Scan bucket with id {} and directory {} ...", bucket.getId(), bucket.getDirectory());
    }
}
