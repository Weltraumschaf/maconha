package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.ScanService;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.shell.Command;
import de.weltraumschaf.maconha.backend.service.scan.shell.CommandFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles the event for hashing a bucket directory.
 * <p>
 * This handler expects a {@link Bucket bucket} as {@link Event#getData() event data}. It stores the bucket globally
 * under the key {@link Global#BUCKET}.
 * </p>
 */
public final class DirHashHandler extends BaseHandler implements EventHandler, HasLogger {

    private final CommandFactory cmds;

    public DirHashHandler(final CommandFactory cmds) {
        super("processing event to hash a bucket directory");
        this.cmds = Validate.notNull(cmds, "cmds");
    }

    @Override
    void doWork(final EventContext context, final Event event) {
        assertPreConditions(context, event, Bucket.class);

        final Bucket bucket = (Bucket) event.getData();
        context.reporter().normal(getClass(), "Dirhashing bucket %s.", bucket);

        final Path directory = Paths.get(bucket.getDirectory());
        final Command dirhash = cmds.dirhash(directory);

        try {
            dirhash.execute();
        } catch (final IOException | InterruptedException e) {
            throw new EventHandlerFailed("Dirhashing of bucket directory '%s' failed!", e, directory);
        }

        context.globals().put(Global.BUCKET, bucket);
        final String checksumFile = bucket.getDirectory() + "/" + ScanService.CHECKSUM_FILE;
        context.emitter().emmit(new Event(EventType.LOAD_FILE_HASHES, checksumFile));
    }

}
