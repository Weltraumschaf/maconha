package de.weltraumschaf.maconha.service.scan;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.service.MediaFileService;
import de.weltraumschaf.maconha.service.ScanService;
import de.weltraumschaf.maconha.service.ScanServiceFactory;
import de.weltraumschaf.maconha.service.ScanStatusService;
import de.weltraumschaf.maconha.service.scan.hashing.HashFileReader;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import de.weltraumschaf.maconha.shell.Command;
import de.weltraumschaf.maconha.shell.Commands;
import de.weltraumschaf.maconha.shell.Result;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Thread executor based implementation.
 * <p>
 * This class is package private because it must not be used directly from outside. Use the DI of Spring to get an
 * instance.
 * </p>
 * <ul>
 * <li><a href="http://www.deadcoderising.com/java8-writing-asynchronous-code-with-completablefuture/">Writing asynchronous code with CompletableFuture</a></li>
 * <li><a href="https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletionStage.html">Interface CompletionStage&lt;T&gt;</a></li>
 * </ul>
 */
@Service(ScanServiceFactory.THREAD)
final class ThreadScanService extends BaseScanService implements ScanService, ScanCallBack {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadScanService.class);

    private final MaconhaConfiguration config;
    private final MediaFileService mediaFiles;
    private final TaskExecutor executor;
    private Commands cmds;

    @Lazy
    @Autowired
    ThreadScanService(final MaconhaConfiguration config, final MediaFileService mediaFiles, final TaskExecutor executor, final ScanStatusService statuses) {
        super(statuses);
        this.config = config;
        this.mediaFiles = mediaFiles;
        this.executor = executor;
    }

    @PostConstruct
    public void initHook() {
        LOGGER.debug("Initialize thread based scan service.");
        cmds = new Commands(Paths.get(config.getBindir()));
    }

    @Override
    public void scan(final Bucket bucket, final UI currentUi) {
        final long id = statuses.nextId();
        final Command dirhash = cmds.dirhash(Paths.get(bucket.getDirectory()));
        final ScanTask task = new ScanTask(id, bucket, currentUi, dirhash, mediaFiles, this);
        final Execution execution = new Execution(id, bucket, currentUi, task);
        executor.execute(task);
        scans.put(id, execution);
    }

    @Override
    public boolean stop(final long executionId) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public List<ScanStatus> overview() {
        final List<ScanStatus> allScans = scans.values()
            .stream()
            .map(this::convert)
            .collect(Collectors.toList());
        allScans.addAll(statuses.allStatuses());
        return allScans;
    }

    @Override
    public void beforeScan(final long id) {
        final Execution execution = getExecution(id);
        execution.start();
        Notification notification = UiNotifier.notification(
            "Scan job started",
            "Scan for bucket '%s' in directory '%s' with id %d started.",
            execution.getBucket().getName(), execution.getBucket().getDirectory(), id);
        UiNotifier.notifyClient(id, notification, execution.getCurrentUi());
    }

    @Override
    public void afterScan(final long id) {
        final Execution execution = getExecution(id);
        execution.stop();
        final String duration = formatDuration(execution.getStartTime(), execution.getStopTime());

        final Notification notification = UiNotifier.notification(
            "Scan job finished",
            "Scan for bucket '%s' in directory '%s' with id %d finished in %s.",
            execution.getBucket().getName(), execution.getBucket().getDirectory(), id, duration);
        UiNotifier.notifyClient(id, notification, execution.getCurrentUi());

        statuses.storeStatus(convert(execution));
        scans.remove(id);
    }

    private ScanStatus convert(final Execution execution) {
        final DateTime startTime = execution.getStartTime();
        final DateTime endTime;
        final String formattedEndTime;

        if (execution.hasStopTime()) {
            endTime = execution.getStopTime();
            formattedEndTime = formatDateTime(endTime);
        } else {
            endTime = DateTime.now();
            formattedEndTime = "-";
        }

        final String jobStatus;

        if (execution.hasStopTime()) {
            jobStatus = "COMPLETED";
        } else {
            if (execution.hasStartTime()) {
                jobStatus = "RUNNING";
            } else {
                jobStatus = "CREATED";
            }
        }

        return new ScanStatus(
            execution.getId(),
            execution.getBucket().getName(),
            formatDateTime(execution.getCreationTime()),
            formatDateTime(execution.getStartTime()),
            formattedEndTime,
            formatDuration(startTime, endTime),
            jobStatus,
            ScanServiceFactory.THREAD);
    }

    private static class ScanTask implements Runnable {

        private final Long id;
        private final Bucket bucket;
        private final UI currentUi;
        private final Command dirhash;
        private final MediaFileService mediaFiles;
        private final ScanCallBack callback;

        ScanTask(final Long id, final Bucket bucket, final UI currentUi, final Command dirhash, final MediaFileService mediaFiles, final ScanCallBack callback) {
            super();
            this.id = id;
            this.bucket = bucket;
            this.currentUi = currentUi;
            this.dirhash = dirhash;
            this.mediaFiles = mediaFiles;
            this.callback = callback;
        }

        @Override
        public void run() {
            callback.beforeScan(id);
            final Result result;

            try {
                result = dirhash.execute();
            } catch (IOException | InterruptedException e) {
                LOGGER.warn(e.getMessage(), e);
                notifyError(e.getMessage());
                return;
            }

            if (result.isFailed()) {
                LOGGER.warn(
                    "Scan job with id {} failed with exit code {} and STDERR: {}",
                    id, result.getExitCode(), result.getStderr());
                notifyError(result.getStderr());
                return;
            }

            LOGGER.debug(result.getStdout());
            final Set<HashedFile> hashedFiles;

            try {
                hashedFiles = new HashFileReader().read(Paths.get(bucket.getDirectory()).resolve(".checksums"));
            } catch (final IOException e) {
                LOGGER.warn(e.getMessage(), e);
                notifyError(e.getMessage());
                return;
            }

            hashedFiles.stream()
                .map(hashedFile -> hashedFile.relativizeFilename(bucket))
                .filter(hashedFile -> mediaFiles.isFileUnseen(hashedFile, bucket))
                .forEach(hashedFile -> mediaFiles.extractAndStoreMetaData(bucket, hashedFile));
            callback.afterScan(id);
        }

        private void notifyError(final String error) {
            final Notification notification = UiNotifier.notification(
                "Scan job failed",
                "Scan for bucket '%s' in directory '%s' failed with error: %s",
                bucket.getName(), bucket.getDirectory(), error);
            UiNotifier.notifyClient(id, notification, currentUi);
        }
    }
}
