package de.weltraumschaf.maconha.backend.service.scanstatus;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.app.MaconhaConfiguration;
import de.weltraumschaf.maconha.backend.service.ScanService;
import de.weltraumschaf.maconha.backend.service.ScanService.ScanStatus;
import de.weltraumschaf.maconha.backend.service.ScanStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Default implementation.
 * <p>
 * This class is package private because it must not be used directly from outside. Use the DI of Spring to get an
 * instance.
 * </p>
 * <p>
 * This implementation is thread safe to allow concurrent store an retrieve of statuses.
 * </p>
 */
@Service
final class DefaultScanStatusService implements ScanStatusService, HasLogger {
    static final String STATUSES_FILE_NAME = "statuses.json";
    static final String STATUSES_DIR_NAME = "scans";

    private final Object lock = new Object();
    private final MaconhaConfiguration config;
    private final StatusSerializer serializer;

    @Autowired
    DefaultScanStatusService(final MaconhaConfiguration config) {
        this(config, new JsonStatusSerializer());
    }

    DefaultScanStatusService(final MaconhaConfiguration config, final StatusSerializer serializer) {
        super();
        this.config = config;
        this.serializer = serializer;
    }

    @Override
    public synchronized Collection<ScanStatus> allStatuses() {
        logger().debug("Get all statuses.");

        synchronized (lock) {
            return Collections.unmodifiableCollection(readStatusFile());
        }
    }

    @Override
    public synchronized void store(final ScanStatus status) {
        Validate.notNull(status, "status");
        logger().debug("Store status {}.", status);

        synchronized (lock) {
            final Collection<ScanStatus> statuses = new ArrayList<>(readStatusFile());
            statuses.add(status);
            storeStatusesToFile(statuses);
        }

        logger().debug("Status deleted.");
    }

    @Override
    public void delete(final ScanStatus status) {
        Validate.notNull(status, "status");
        logger().debug("Delete statuses {}.", status);

        synchronized (lock) {
            final Collection<ScanStatus> statuses = new ArrayList<>(readStatusFile());
            statuses.remove(status);
            storeStatusesToFile(statuses);
        }

        logger().debug("Status deleted.");
    }

    @Override
    public void deleteAll() {
        final Path statusFile = resolveStatusFile();
        logger().debug("Delete all statuses from file {}.", statusFile);

        synchronized (lock) {
            if (Files.exists(statusFile)) {
                try {
                    Files.delete(statusFile);
                    logger().debug("Statuses file {} deleted.", statusFile);
                } catch (final IOException e) {
                    logger().error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public synchronized long nextId() {
        synchronized (lock) {
            return readStatusFile()
                .stream()
                .mapToLong(ScanStatus::getId)
                .max()
                .orElse(0L) + 1;
        }
    }

    private Collection<ScanStatus> readStatusFile() {
        final Path statusFile = resolveStatusFile();
        logger().debug("Read statuses from file {}.", statusFile);

        if (Files.exists(statusFile)) {
            try (final Reader reader = Files.newBufferedReader(statusFile)) {
                logger().debug("Loading stored statuses.");
                return serializer.deserialize(reader);
            } catch (final IOException e) {
                logger().error(e.getMessage(), e);
            }
        } else {
            logger().warn("There is no such status file '{}' to load.", statusFile);
        }

        return new ArrayList<>();
    }

    private void storeStatusesToFile(final Collection<ScanStatus> statuses) {
        logger().warn("Persist statuses.");
        final Path statusDir = resolveStatusDir();

        if (!Files.exists(statusDir)) {
            logger().debug("Create directory '{}' to store status file.", statusDir);

            try {
                Files.createDirectories(statusDir);
            } catch (final IOException e) {
                logger().error(e.getMessage(), e);
                return;
            }
        }

        final Path statusFile = resolveStatusFile();
        try (final BufferedWriter writer = Files.newBufferedWriter(statusFile)) {
            logger().debug("Store statuses in file {}.", statusFile);
            serializer.serialize(statuses, writer);
        } catch (final IOException e) {
            logger().error(e.getMessage(), e);
        }
    }

    private Path resolveStatusFile() {
        return resolveStatusDir().resolve(STATUSES_FILE_NAME);
    }

    private Path resolveStatusDir() {
        return Paths.get(config.getHomedir()).resolve(STATUSES_DIR_NAME);
    }
}
