package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.service.ScanService;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Common functionality for scan services.
 */
abstract class BaseScanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseScanService.class);

    final PeriodFormatter secondsFormat =
        new PeriodFormatterBuilder()
            .printZeroAlways()
            .minimumPrintedDigits(2)
            .appendHours()
            .appendSeparator(":")
            .appendMinutes()
            .appendSeparator(":")
            .appendSeconds()
            .toFormatter();
    final DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("HH:mm:ss MM.dd.yy");

    final Collection<ScanService.ScanStatus> statuses = new CopyOnWriteArrayList<>();
    final Map<Long, Execution> scans = new ConcurrentHashMap<>();
    final MaconhaConfiguration config;
    private final StatusSerializer serializer = new StatusSerializer();

    BaseScanService(final MaconhaConfiguration config) {
        super();
        this.config = config;
    }

    @PostConstruct
    public void init() {
        LOGGER.debug("Initialize service.");
        readStatuses();
        initHook();
    }

    void initHook() {}

    @PreDestroy
    public void deinit() {
        LOGGER.debug("Deinitialize service.");
        storeStatuses();
        deinitHook();
    }

    void deinitHook() {}

    private Path resolveStatusFile() {
        return resolveStatusDir().resolve("statuses.json");
    }

    private Path resolveStatusDir() {
        return Paths.get(config.getHomedir()).resolve("scans");
    }

    private void readStatuses() {
        LOGGER.debug("Restore statuses from file.");
        final Path stausFile = resolveStatusFile();

        if (Files.exists(stausFile)) {
            try (final Reader reader = Files.newBufferedReader(stausFile)) {
                LOGGER.debug("Loading stored statuses.");
                statuses.addAll(serializer.deserialize(reader));
            } catch (final IOException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        } else {
            LOGGER.debug("There is no such status file '{}' to load.", stausFile);
        }
    }

    private void storeStatuses() {
        LOGGER.warn("Persist statuses.");
        final Path statusDir = resolveStatusDir();

        if (!Files.exists(statusDir)) {
            LOGGER.debug("Create directory '{}' to store status file.", statusDir);

            try {
                Files.createDirectories(statusDir);
            } catch (IOException e) {
                LOGGER.warn(e.getMessage(), e);
                return;
            }
        }

        final Path statusFile = resolveStatusFile();
        try (final BufferedWriter writer = Files.newBufferedWriter(statusFile)) {
            LOGGER.debug("Store statuses in file {}.", statusFile);
            serializer.serialize(statuses, writer);
        } catch (final IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    final Execution getExecution(final Long id) {
        if (scans.containsKey(id)) {
            return scans.get(id);
        }

        throw new ScanService.ScanError("There's no such job with id %d!", id);
    }
}
