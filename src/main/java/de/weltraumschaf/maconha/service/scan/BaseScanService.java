package de.weltraumschaf.maconha.service.scan;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.service.ScanService;
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

    abstract void initHook();

    @PreDestroy
    public void deinit() {
        storeStatuses();
        deinitHook();
    }

    abstract void deinitHook();

    Notification notification(final String caption, final String description, final Object... args) {
        return new Notification(caption, String.format(description, args), Notification.Type.TRAY_NOTIFICATION);
    }

    final void notifyClient(final Long jobId, final Notification notification, final UI ui) {
        if (ui == null) {
            LOGGER.warn("Currents UI null! Can't notify client about job with id {}.", jobId);
        } else {
            ui.access(() -> {
                final Page page = ui.getPage();

                if (page == null) {
                    LOGGER.warn("Currents page null! Can't notify client about job with id {}.", jobId);
                } else {
                    notification.show(page);
                }
            });
        }
    }

    private Path resolveStausFile() {
        return resolveStausDir().resolve("statuses.json");
    }

    private Path resolveStausDir() {
        return Paths.get(config.getHomedir()).resolve("scans");
    }

    private void readStatuses() {
        final Path stausFile = resolveStausFile();

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
        final Path stausDir = resolveStausDir();

        if (!Files.exists(stausDir)) {
            LOGGER.debug("Create directory '{}' to store status file.", stausDir);

            try {
                Files.createDirectories(stausDir);
            } catch (IOException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }

        try (final BufferedWriter writer = Files.newBufferedWriter(resolveStausFile())) {
            LOGGER.debug("Store statuses.");
            new Gson().toJson(statuses, writer);
        } catch (final IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

}
