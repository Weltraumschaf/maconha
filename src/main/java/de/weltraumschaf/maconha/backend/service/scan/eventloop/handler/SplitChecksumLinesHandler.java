package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.Event;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventContext;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventHandler;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventType;

/**
 * Handles the event for splitting the checksum file content by line.
 * <p>
 * Expects a string with the file content as {@link Event#getData() event data}.
 * </p>
 */
public final class SplitChecksumLinesHandler extends BaseHandler implements EventHandler, HasLogger {
    @Override
    public void process(final EventContext context, final Event event) {
        assertPreConditions(context, event, String.class);

        logger().debug("Split checkusm file content ...");
        final String checksums = (String) event.getData();

        for (final String line : checksums.split("\\r?\\n")) {
            context.emitter().emmit(new Event(EventType.PARSE_CHECKSUM_LINE, line));
        }
    }
}
