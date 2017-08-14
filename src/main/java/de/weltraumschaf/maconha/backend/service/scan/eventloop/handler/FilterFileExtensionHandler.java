package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.FileExtension;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;

/**
 * Filter out files with extension we don not want to scan.
 * <p>
 * Expects a {@link HashedFile hashed file} as {@link Event#getData() event data} and expects a {@link Bucket bucket}
 * as global data under the key {@link Global#BUCKET}.
 * </p>
 */
public final class FilterFileExtensionHandler extends BaseHandler implements EventHandler, HasLogger {
    @Override
    public void process(final EventContext context, final Event event) {
        assertPreConditions(context, event, HashedFile.class);
        final HashedFile hashedFile = (HashedFile) event.getData();
        logger().debug("Filter file extension for {} ...", hashedFile);

        final String extension = FileExtension.extractExtension(hashedFile.getFile());

        if (FileExtension.hasValue(extension)) {
            context.emitter().emmit(new Event(EventType.RELATIVIZE_HASHED_FILE, hashedFile));
        } else {
            logger().debug("Filter out file {}", hashedFile.getFile());
        }

    }
}
