package de.weltraumschaf.maconha.backend.service.scanreport;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.app.MaconhaConfiguration;
import de.weltraumschaf.maconha.backend.service.ScanReportService;
import de.weltraumschaf.maconha.backend.service.ScanService.ScanStatus;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Default implementation.
 */
@Service
final class DefaultScanReportService implements ScanReportService, HasLogger {
    private static final String REPORTS_FILE_NAME = "scan_report_%d.json";
    private static final String REPORT_DIR_NAME = "reports";

    private final Object lock = new Object();
    private final MaconhaConfiguration config;
    private final ReportSerializer serializer;

    @Autowired
    DefaultScanReportService(final MaconhaConfiguration config) {
        this(config, new JsonReportSerializer());
    }

    DefaultScanReportService(final MaconhaConfiguration config, final ReportSerializer serializer) {
        super();
        this.config = config;
        this.serializer = serializer;
    }

    @Override
    public void store(final ScanStatus status, final Report report) {
        Validate.notNull(status, "status");
        Validate.notNull(report, "report");
        final Path reportDir = resolveReportDir();

        synchronized (lock) {
            if (!Files.exists(reportDir)) {
                logger().debug("Create directory '{}' to store report files.", reportDir);

                try {
                    Files.createDirectories(reportDir);
                } catch (final IOException e) {
                    logger().error(e.getMessage(), e);
                    return;
                }
            }
        }

        final Path reportFile = resolveReportFile(status.getId());

        synchronized (lock) {
            try (final BufferedWriter writer = Files.newBufferedWriter(reportFile)) {
                logger().debug("Store report in file {}.", reportFile);
                serializer.serialize(report, writer);
            } catch (final IOException e) {
                logger().error(e.getMessage(), e);
            }
        }
    }

    @Override
    public Report load(final ScanStatus status) {
        Validate.notNull(status, "status");
        final Path reportFile = resolveReportFile(status.getId());

        synchronized (lock) {
            if (Files.exists(reportFile)) {
                try (final Reader reader = Files.newBufferedReader(reportFile)) {
                    logger().debug("Loading report for status {}.", status);
                    return serializer.deserialize(reader);
                } catch (final IOException e) {
                    logger().error(e.getMessage(), e);
                }
            } else {
                logger().warn("There is no such report file '{}' to load.", reportFile);
            }

            return Report.EMPTY;
        }
    }

    @Override
    public void delete(final ScanStatus status) {
        Validate.notNull(status, "status");
        final Path reportFile = resolveReportFile(status.getId());

        synchronized (lock) {
            if (Files.exists(reportFile)) {
                try {
                    Files.delete(reportFile);
                    logger().debug("Report file {} deleted.", reportFile);
                } catch (final IOException e) {
                    logger().error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public void deleteAll() {
        synchronized (lock) {

        }
    }

    private Path resolveReportFile(final long statusId) {
        return resolveReportDir().resolve(String.format(REPORTS_FILE_NAME, statusId));
    }

    private Path resolveReportDir() {
        return Paths.get(config.getHomedir()).resolve(REPORT_DIR_NAME);
    }
}
