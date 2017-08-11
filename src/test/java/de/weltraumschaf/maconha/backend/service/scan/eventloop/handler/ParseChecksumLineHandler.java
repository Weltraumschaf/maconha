package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.Event;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventContext;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventHandler;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventType;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFileLineParser;

/**
 *
 */
public final class ParseChecksumLineHandler implements EventHandler, HasLogger {
    @Override
    public void process(final EventContext context, final Event event) {
        final String line = (String)event.getData();
        logger().debug("Parse checksum line {} ...", line);
        final HashedFileLineParser parser = new HashedFileLineParser();
        final HashedFile hashedFile = parser.parse(line);
        context.emitter().emmit(new Event(EventType.RELATIVIZE_FILE, hashedFile));

    }
}
