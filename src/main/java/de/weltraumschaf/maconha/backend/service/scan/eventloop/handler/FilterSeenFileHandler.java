package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.MediaFileService;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;

/**
 * Handles the event for filtering out already seen {@link HashedFile hashed file}.
 * <p>
 * Expects a {@link HashedFile hashed file} as {@link Event#getData() event data} and expects a {@link Bucket bucket}
 * as global data under the key {@link Global#BUCKET}.
 * </p>
 */
public final class FilterSeenFileHandler extends BaseHandler implements EventHandler, HasLogger {
    private final MediaFileService mediaFiles;

    public FilterSeenFileHandler(final MediaFileService mediaFiles) {
        super("processing event to filter out already seen file");
        this.mediaFiles = Validate.notNull(mediaFiles, "mediaFiles");
    }

    @Override
    void doWork(final EventContext context, final Event event) {
        assertPreConditions(context, event, HashedFile.class);

        final HashedFile hashedFile = (HashedFile) event.getData();
        context.reporter().normal(getClass(),
            "Check if file %s with hash %s is unseen.", hashedFile.getFile(), hashedFile.getHash());

        if (!(context.globals().get(Global.BUCKET) instanceof Bucket)) {
            throw new IllegalGlobalData("The passed in event data is not of type %s!",
                Bucket.class.getName());
        }

        final Bucket bucket = (Bucket) context.globals().get(Global.BUCKET);

        if (mediaFiles.isFileUnseen(hashedFile, bucket)) {
            context.emitter().emmit(new Event(EventType.EXTRACT_FILE_META_DATA, hashedFile));
        } else {
            context.reporter().normal(getClass(),
                "File %s with hash %s is already seen.", hashedFile.getFile(), hashedFile.getHash());
        }
    }
}
