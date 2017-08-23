package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

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
public final class LoadFileHashesHandler extends BaseHandler implements EventHandler, HasLogger {
    public LoadFileHashesHandler() {
        super("processing event to load files with hashes");
    }

    @Override
    void doWork(final EventContext context, final Event event) {
        assertPreConditions(context, event, String.class);

        if (!(event.getData() instanceof String)) {
            throw new IllegalEventData(
                "The passed in event data is not of type %s!",
                String.class.getSimpleName());
        }

        final String file = (String) event.getData();
        context.reporter().normal(getClass(),"Loading hashes from %s.", file);
        final Path checksumFiles = Paths.get(file);
        final String checksums;

        try {
            checksums = new String(Files.readAllBytes(checksumFiles));
        } catch (final IOException e) {
            throw new EventHandlerFailed("Can't read checksum file '%s'!", e, checksumFiles);
        }

        context.emitter().emmit(new Event(EventType.SPLIT_CHECKSUM_LINES, checksums));
    }
}
