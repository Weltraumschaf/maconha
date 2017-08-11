package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;

/**
 *
 */
public final class RelativizeFileHandler implements EventHandler, HasLogger {
    @Override
    public void process(final EventContext context, final Event event) {
        final HashedFile hashedFile = (HashedFile) event.getData();
        logger().debug("Relativize file {} ...", hashedFile);
        final Bucket bucket = (Bucket) context.globals().get(Global.BUCKET);
        context.emitter().emmit(new Event(EventType.FILTER_SEEN_FILE, hashedFile.relativizeFilename(bucket)));

    }
}
