package de.weltraumschaf.maconha.service.scanstatus;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.service.ScanService;
import de.weltraumschaf.maconha.service.ScanStatusService;
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
final class DefaultScanStatusService implements ScanStatusService {
    static final String STATUSES_FILE_NAME = "statuses.json";
    static final String STATUSES_DIR_NAME = "scans";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultScanStatusService.class);

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
    public synchronized Collection<ScanService.ScanStatus> allStatuses() {
        return Collections.unmodifiableCollection(readStatusFile());
    }

    @Override
    public synchronized void storeStatus(final ScanService.ScanStatus status) {
        Validate.notNull(status, "status");
        final Collection<ScanService.ScanStatus> statuses = new ArrayList<>();
        statuses.addAll(readStatusFile());
        statuses.add(status);
        storeStatusesToFile(statuses);
    }


    private Collection<ScanService.ScanStatus> readStatusFile() {
        final Path statusFile = resolveStatusFile();
        LOGGER.debug("Read statuses from file {}.", statusFile);

        if (Files.exists(statusFile)) {
            try (final Reader reader = Files.newBufferedReader(statusFile)) {
                LOGGER.debug("Loading stored statuses.");
                return serializer.deserialize(reader);
            } catch (final IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            LOGGER.debug("There is no such status file '{}' to load.", statusFile);
        }

        return new ArrayList<>();
    }

    private void storeStatusesToFile(final Collection<ScanService.ScanStatus> statuses) {
        LOGGER.warn("Persist statuses.");
        final Path statusDir = resolveStatusDir();

        if (!Files.exists(statusDir)) {
            LOGGER.debug("Create directory '{}' to store status file.", statusDir);

            try {
                Files.createDirectories(statusDir);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                return;
            }
        }

        final Path statusFile = resolveStatusFile();
        try (final BufferedWriter writer = Files.newBufferedWriter(statusFile)) {
            LOGGER.debug("Store statuses in file {}.", statusFile);
            serializer.serialize(statuses, writer);
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private Path resolveStatusFile() {
        return resolveStatusDir().resolve(STATUSES_FILE_NAME);
    }

    private Path resolveStatusDir() {
        return Paths.get(config.getHomedir()).resolve(STATUSES_DIR_NAME);
    }
}
