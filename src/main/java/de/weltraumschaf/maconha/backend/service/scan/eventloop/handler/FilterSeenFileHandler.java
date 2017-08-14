package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.MediaFileService;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;

/**
 * Handles the event for filtering out already seen {@link HashedFile hashed file}.
 * <p></p>
 */
public final class FilterSeenFileHandler extends BaseHandler implements EventHandler, HasLogger {
    private final MediaFileService mediaFiles;

    public FilterSeenFileHandler(final MediaFileService mediaFiles) {
        super();
        this.mediaFiles = Validate.notNull(mediaFiles, "mediaFiles");
    }

    @Override
    public void process(final EventContext context, final Event event) {
        assertPreConditions(context, event, HashedFile.class);

        final HashedFile hashedFile = (HashedFile) event.getData();
        logger().debug("Check if file {} is unseen ...", hashedFile);

        if (!(context.globals().get(Global.BUCKET) instanceof Bucket)) {
            throw new IllegalGlobalData("The passed in event data is not of type %s!",
                Bucket.class.getName());
        }

        final Bucket bucket = (Bucket) context.globals().get(Global.BUCKET);

        if (mediaFiles.isFileUnseen(hashedFile, bucket)) {
            context.emitter().emmit(new Event(EventType.EXTRACT_FILE_META_DATA, hashedFile));
        }
    }
}
