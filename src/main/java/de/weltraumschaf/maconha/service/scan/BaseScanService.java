package de.weltraumschaf.maconha.service.scan;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
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
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    final MaconhaConfiguration config;
    final Collection<ScanService.ScanStatus> statuses = new CopyOnWriteArrayList<>();
    final Map<Long, Execution> scans = new ConcurrentHashMap<>();

    BaseScanService(final MaconhaConfiguration config) {
        super();
        this.config = config;
    }

    @PostConstruct
    public void init() {
        readStatuses();
        initHook();
    }

    void initHook() {}

    @PreDestroy
    public void deinit() {
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
        final Path stausFile = resolveStatusFile();

        if (Files.exists(stausFile)) {
            try (final Reader reader = Files.newBufferedReader(stausFile)) {
                LOGGER.debug("Loading stored statuses.");
                final Type type = new TypeToken<ArrayList<ScanService.ScanStatus>>() {
                }.getType();
                statuses.addAll(new Gson().fromJson(reader, type));
            } catch (final IOException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        } else {
            LOGGER.debug("There is no such status file '{}' to load.", stausFile);
        }
    }

    private void storeStatuses() {
        final Path stausDir = resolveStatusDir();

        if (!Files.exists(stausDir)) {
            LOGGER.debug("Create directory '{}' to store status file.", stausDir);

            try {
                Files.createDirectories(stausDir);
            } catch (IOException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }

        try (final BufferedWriter writer = Files.newBufferedWriter(resolveStatusFile())) {
            LOGGER.debug("Store statuses.");
            new Gson().toJson(statuses, writer);
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
