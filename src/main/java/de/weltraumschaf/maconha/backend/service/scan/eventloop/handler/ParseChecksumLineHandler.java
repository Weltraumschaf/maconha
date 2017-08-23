package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.Event;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventContext;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventHandler;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventType;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFileLineParser;

/**
 * Handles the event for parsing a checksum file line.
 * <p>
 * Expects a string with the line as {@link Event#getData() event data}.
 * </p>
 */
public final class ParseChecksumLineHandler extends BaseHandler implements EventHandler, HasLogger {
    public ParseChecksumLineHandler() {
        super("processing event to parse a checksum line");
    }

    @Override
    void doWork(final EventContext context, final Event event) {
        assertPreConditions(context, event, String.class);

        final String line = (String) event.getData();
        context.reporter().normal(getClass(),"Parse checksum line %s.", line);
        final HashedFileLineParser parser = new HashedFileLineParser();
        context.emitter().emmit(new Event(EventType.FILTER_FILE_EXTENSION, parser.parse(line)));
    }
}
