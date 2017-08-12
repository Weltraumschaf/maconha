package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.Event;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventContext;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventHandler;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;

/**
 *
 */
public final class FilterSeenFileHandler implements EventHandler, HasLogger{
    @Override
    public void process(final EventContext context, final Event event) {
        final HashedFile hashedFile = (HashedFile) event.getData();
        logger().debug("Check if file " + hashedFile + " is unseen ...");

    }
}
