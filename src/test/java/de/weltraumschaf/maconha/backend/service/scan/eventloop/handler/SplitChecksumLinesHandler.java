package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.Event;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventContext;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventHandler;

/**
 *
 */
final class SplitChecksumLinesHandler implements EventHandler, HasLogger {
    @Override
    public void process(final EventContext context, final Event event) {
        logger().debug("Split checkusm file content ...");
        final String checksums = (String) event.getData();

        for (final String line : checksums.split("\\r?\\n")) {
            context.emitter().emmit(new Event(EventType.PARSE_CHECKSUM_LINE, line));
        }
    }
}
