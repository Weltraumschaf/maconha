package de.weltraumschaf.maconha.service.scan;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.model.*;
import de.weltraumschaf.maconha.service.MediaFileService;
import de.weltraumschaf.maconha.service.ScanService;
import de.weltraumschaf.maconha.service.ScanServiceFactory;
import de.weltraumschaf.maconha.service.scan.hashing.HashFileReader;
import de.weltraumschaf.maconha.shell.Commands;
import de.weltraumschaf.maconha.shell.Result;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Thread executor based implementation.
 * <p>
 * This class is package private because it must not be used directly from outside. Use the DI of Spring to get an
 * instance.
 * </p>
 */
@Service(ScanServiceFactory.THREAD)
final class ThreadScanService extends BaseScanService implements ScanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadScanService.class);

    private final MaconhaConfiguration config;
    private final MediaFileService mediaFiles;
    private final TaskExecutor executor;
    private Commands cmds;

    @Lazy
    @Autowired
    ThreadScanService(final MaconhaConfiguration config, final MediaFileService mediaFiles, final TaskExecutor executor) {
        super();
        this.config = config;
        this.mediaFiles = mediaFiles;
        this.executor = executor;
    }

    @PostConstruct
    public void init() {
        LOGGER.debug("Initialize thread based scan service.");
        cmds = new Commands(Paths.get(config.getBindir()));
    }

    @Async
    @Override
    @Transactional
    public void scan(final Bucket bucket, final UI currentUi) {
        long id = 0L;
        Notification notification = notification(
            "Scan job started",
            "Scan for bucket '%s' in directory '%s' with id %d started.",
            bucket.getName(), bucket.getDirectory(), id);
        notifyClient(id, notification, currentUi);
        final DateTime startTime = DateTime.now();

        try {
            scanImpl(id, bucket, currentUi);
//            executor.execute(new ScanTask());
        } catch (final IOException | InterruptedException e) {
            LOGGER.warn(e.getMessage(), e);
            notification = notification(
                "Scan job failed",
                "Scan for bucket '%s' in directory '%s' failed with error: %s",
                bucket.getName(), bucket.getDirectory(), e.getMessage());
            notifyClient(id, notification, currentUi);
            return;
        }

        final DateTime endTime = DateTime.now();
        final String duration = secondsFormat.print(new Duration(startTime, endTime).toPeriod());
        notification = notification(
            "Scan job finished",
            "Scan for bucket '%s' in directory '%s' with id %d finished in %s.",
            bucket.getName(), bucket.getDirectory(), id, duration);
        notifyClient(id, notification, currentUi);
    }

    private void scanImpl(final long id, final Bucket bucket, final UI currentUi) throws IOException, InterruptedException {
        final Result result = cmds.dirhash(Paths.get(bucket.getDirectory())).execute();

        if (result.isFailed()) {
            LOGGER.warn("Scan job with id {} failed with exit code {} and STDERR: {}", id, result.getExitCode(), result.getStderr());
            final Notification notification = notification(
                "Scan job failed",
                "Scan for bucket '%s' in directory '%s' failed with error: %s",
                bucket.getName(), bucket.getDirectory(), result.getStderr());
            notifyClient(id, notification, currentUi);
            return;
        }

        LOGGER.debug(result.getStdout());
        new HashFileReader().read(Paths.get(bucket.getDirectory()).resolve(".checksums")).stream()
            .map(hashedFile -> hashedFile.relativizeFilename(bucket))
            .filter(hashedFile -> mediaFiles.isFileUnseen(hashedFile, bucket))
            .forEach(hashedFile -> mediaFiles.extractAndStoreMetaData(bucket, hashedFile));
    }

    @Override
    public boolean stop(final long executionId) {
        return false;
    }

    @Override
    public List<ScanStatus> overview() {
        return Collections.emptyList();
    }

    private static class ScanTask implements Runnable {

        @Override
        public void run() {

        }
    }
}
