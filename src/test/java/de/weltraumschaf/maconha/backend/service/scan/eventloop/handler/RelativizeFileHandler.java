package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.Event;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventContext;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventHandler;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;

/**
 *
 */
final class RelativizeFileHandler implements EventHandler, HasLogger {
    @Override
    public void process(final EventContext context, final Event event) {
        final HashedFile hashedFile = (HashedFile) event.getData();
        logger().debug("Relativize file {} ...", hashedFile);
        final Bucket bucket = (Bucket) context.globals().get(Globals.BUCKET);
        context.emitter().emmit(new Event(EventType.FILTER_SEEN_FILE, hashedFile.relativizeFilename(bucket)));

    }
}
