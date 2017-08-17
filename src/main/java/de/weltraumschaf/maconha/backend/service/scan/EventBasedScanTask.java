package de.weltraumschaf.maconha.backend.service.scan;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.MediaFileService;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.Event;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventLoop;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventLoopError;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventType;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.handler.*;
import de.weltraumschaf.maconha.backend.service.scan.shell.CommandFactory;

/**
 * Implementation based on {@link de.weltraumschaf.maconha.backend.service.scan.eventloop.EventLoop}.
 */
final class EventBasedScanTask implements ScanTask, HasLogger {
    private final EventLoop loop = new EventLoop();
    private final Long id;
    private final Bucket bucket;
    private final UI currentUi;
    private final CommandFactory cmds;
    private final MediaFileService mediaFiles;
    private final ScanCallBack callback;

    EventBasedScanTask(final Long id, final Bucket bucket, final UI currentUi, final CommandFactory cmds, final MediaFileService mediaFiles, final ScanCallBack callback) {
        super();
        this.id = id;
        this.bucket = bucket;
        this.currentUi = currentUi;
        this.cmds = cmds;
        this.mediaFiles = mediaFiles;
        this.callback = callback;
        registerHandlers();
    }

    private void registerHandlers() {
        loop.register(EventType.DIR_HASH, new DirHashHandler(cmds));
        loop.register(EventType.LOAD_FILE_HASHES, new LoadFileHashesHandler());
        loop.register(EventType.SPLIT_CHECKSUM_LINES, new SplitChecksumLinesHandler());
        loop.register(EventType.PARSE_CHECKSUM_LINE, new ParseChecksumLineHandler());
        loop.register(EventType.FILTER_FILE_EXTENSION, new FilterFileExtensionHandler());
        loop.register(EventType.RELATIVIZE_HASHED_FILE, new RelativizeFileHandler());
        loop.register(EventType.FILTER_SEEN_HASHED_FILE, new FilterSeenFileHandler(mediaFiles));
        loop.register(EventType.EXTRACT_FILE_META_DATA, new ExtractFileMetaDataHandler());
        loop.register(EventType.EXTRACT_KEYWORDS_FROM_FILE_NAME, new ExtractKeywordsFromFileNameHandler());
        loop.register(EventType.EXTRACT_KEYWORDS_FROM_META_DATA, new ExtractKeywordsFromMetaDataHandler());
        loop.register(EventType.FILTER_UNWANTED_KEYWORDS, new FilterUnwantedKeywords());
        loop.register(EventType.STORE_FILE_AND_KEYWORDS, new StoreFileAndKeywordsHandler(mediaFiles));
    }

    @Override
    public void stop() {
        loop.stop();
    }

    @Override
    public void run() {
        callback.beforeScan(id);

        try {
            loop.start(new Event(EventType.DIR_HASH, bucket));
        } catch (final EventLoopError e) {
            logger().error(e.getMessage(), e);
            final Notification notification = UiNotifier.notification(
                "Scan job failed",
                "Scan for bucket '%s' in directory '%s' failed with error: %s",
                bucket.getName(), bucket.getDirectory(), e.getMessage());
            UiNotifier.notifyClient(id, notification, currentUi);
        }

        callback.afterScan(id);
    }
}
