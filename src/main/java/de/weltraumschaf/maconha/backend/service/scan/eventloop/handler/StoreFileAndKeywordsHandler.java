package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.MediaFileService;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;

/**
 * Handles the event for storing the file and its keywords.
 * <p>
 * Expects a {@link MediaDataCollector hashed file} as {@link Event#getData() event data} and expects a {@link Bucket bucket}
 * as global data under the key {@link Global#BUCKET}.
 * </p>
 * <p>
 * This handler does not emit any more events.
 * </p>
 */
public final class StoreFileAndKeywordsHandler extends BaseHandler implements EventHandler, HasLogger {
    private final MediaFileService mediaFiles;

    public StoreFileAndKeywordsHandler(final MediaFileService mediaFiles) {
        super("processing event to store a media file and its keywords");
        this.mediaFiles = Validate.notNull(mediaFiles, "mediaFiles");
    }

    @Override
    void doWork(final EventContext context, final Event event) {
        assertPreConditions(context, event, MediaDataCollector.class);

        final MediaDataCollector collector = (MediaDataCollector) event.getData();
        final HashedFile hashedFile = collector.getFile();
        context.reporter().normal(getClass(),"Storing media file %s.", hashedFile.getFile());

        if (!(context.globals().get(Global.BUCKET) instanceof Bucket)) {
            throw new IllegalGlobalData("The passed in event data is not of type %s!",
                Bucket.class.getName());
        }

        final Bucket bucket = (Bucket) context.globals().get(Global.BUCKET);

        mediaFiles.storeMetaData(
            bucket,
            collector.getFile(),
            collector.getMetaData().getMime(),
            collector.getKeywords());
    }
}
