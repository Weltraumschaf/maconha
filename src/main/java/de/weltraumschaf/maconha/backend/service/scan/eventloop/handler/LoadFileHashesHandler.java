package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles the event for loading a checksum file from bucket directory.
 * <p>
 * Expects a string with the file path as {@link Event#getData() event data}.
 * </p>
 */
public final class LoadFileHashesHandler implements EventHandler, HasLogger {
    @Override
    public void process(final EventContext context, final Event event) {
        Validate.notNull(context, "context");
        Validate.notNull(event, "event");

        if (event.getData() == null) {
            throw new IllegalEventData("The passed in event has no data (is null)!");
        }

        if (!(event.getData() instanceof String)) {
            throw new IllegalEventData(
                "The passed in event data is not of type %s!",
                String.class.getSimpleName());
        }

        final String data = (String) event.getData();
        logger().debug("Loading hashes from {} ...", data);
        final Path checksumFiles = Paths.get(data);
        final String checksums;

        try {
            checksums = new String(Files.readAllBytes(checksumFiles));
        } catch (final IOException e) {
            throw new EventHandlerFailed("Can't read checksum file '%s'!", e, checksumFiles);
        }

        context.emitter().emmit(new Event(EventType.SPLIT_CHECKSUM_LINES, checksums));
    }
}
