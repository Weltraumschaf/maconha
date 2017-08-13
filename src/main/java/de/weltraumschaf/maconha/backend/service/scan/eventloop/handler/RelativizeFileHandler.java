package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;

/**
 * Handles the event for relative a the path aif a {@link HashedFile hashed file}.
 * <p>
 * Expects a {@link HashedFile hashed file} as {@link Event#getData() event data} and expects a {@link Bucket bucket}
 * as global data under the key {@link Global#BUCKET}.
 * </p>
 */
public final class RelativizeFileHandler extends BaseHandler implements EventHandler, HasLogger {
    @Override
    public void process(final EventContext context, final Event event) {
        assertPreConditions(context, event, HashedFile.class);
        final HashedFile hashedFile = (HashedFile) event.getData();
        logger().debug("Relativize file {} ...", hashedFile);

        if (!(context.globals().get(Global.BUCKET) instanceof Bucket)) {
            throw new IllegalGlobalData("The passed in event data is not of type %s!",
                Bucket.class.getName());
        }

        final Bucket bucket = (Bucket) context.globals().get(Global.BUCKET);
        context.emitter().emmit(new Event(EventType.FILTER_SEEN_HASHED_FILE, hashedFile.relativizeFilename(bucket)));
    }
}
