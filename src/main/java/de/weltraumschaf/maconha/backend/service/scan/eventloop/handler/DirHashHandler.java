package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;

/**
 *
 */
public final class DirHashHandler implements EventHandler, HasLogger {
    @Override
    public void process(final EventContext context, final Event event) {
        final Bucket bucket = (Bucket) event.getData();
        logger().debug("Dirhashing bucket {} ...", bucket);
        context.globals().put(Global.BUCKET, bucket);
        context.emitter().emmit(new Event(EventType.LOAD_FILE_HASHES, event.getData() + "/.checksums"));
    }
}
