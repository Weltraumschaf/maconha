package de.weltraumschaf.maconha.backend.service.scan;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.MediaFileService;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashFileReader;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import de.weltraumschaf.maconha.backend.service.scan.shell.Command;
import de.weltraumschaf.maconha.backend.service.scan.shell.CommandFactory;
import de.weltraumschaf.maconha.backend.service.scan.shell.Result;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * Default implementation.
 */
final class DefaultScanTask implements ScanTask, HasLogger {
    private final Long id;
    private final Bucket bucket;
    private final UI currentUi;
    private final Command dirhash;
    private final MediaFileService mediaFiles;
    private final ScanCallBack callback;

    DefaultScanTask(final Long id, final Bucket bucket, final UI currentUi, final CommandFactory cmds, final MediaFileService mediaFiles, final ScanCallBack callback) {
        super();
        this.id = id;
        this.bucket = bucket;
        this.currentUi = currentUi;
        this.dirhash = cmds.dirhash(Paths.get(bucket.getDirectory()));
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
            logger().warn(e.getMessage(), e);
            notifyError(e.getMessage());
            return;
        }

        if (result.isFailed()) {
            logger().warn(
                "Scan job with id {} failed with exit code {} and STDERR: {}",
                id, result.getExitCode(), result.getStderr());
            notifyError(result.getStderr());
            return;
        }

        logger().debug(result.getStdout());
        final Set<HashedFile> hashedFiles;

        try {
            final Path checksums = Paths.get(bucket.getDirectory()).resolve(ThreadScanService.CHECKSUM_FILE);
            hashedFiles = new HashFileReader().read(checksums);
        } catch (final IOException e) {
            logger().warn(e.getMessage(), e);
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

    @Override
    public void stop() {
        // This implementation is not stoppable.
    }
}
