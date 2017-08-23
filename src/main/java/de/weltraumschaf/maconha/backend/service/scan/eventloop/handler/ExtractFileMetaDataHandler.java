package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.FileMetaData;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.mediafile.Extractor;
import de.weltraumschaf.maconha.backend.service.mediafile.MetaDataExtractor;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles the event for extracting meta data from a {@link HashedFile hashed file}.
 * <p>
 * Expects a {@link HashedFile hashed file} as {@link Event#getData() event data} and expects a {@link Bucket bucket}
 * as global data under the key {@link Global#BUCKET}.
 * </p>
 */
public final class ExtractFileMetaDataHandler extends BaseHandler implements EventHandler, HasLogger {
    private final Extractor<FileMetaData> extractor;

    public ExtractFileMetaDataHandler() {
        this(new MetaDataExtractor());
    }

    ExtractFileMetaDataHandler(final Extractor<FileMetaData> extractor) {
        super("processing event to extract metadata from a file");
        this.extractor = Validate.notNull(extractor, "extractor");
    }

    @Override
    void doWork(final EventContext context, final Event event) {
        assertPreConditions(context, event, HashedFile.class);
        final HashedFile hashedFile = (HashedFile) event.getData();
        context.reporter().normal(getClass(), "Extracting meta data from %s.", hashedFile.getFile());

        if (!(context.globals().get(Global.BUCKET) instanceof Bucket)) {
            throw new IllegalGlobalData("The passed in event data is not of type %s!",
                Bucket.class.getName());
        }

        final Bucket bucket = (Bucket) context.globals().get(Global.BUCKET);
        final Path absoluteFile;

        try {
            absoluteFile = Paths.get(bucket.getDirectory()).resolve(hashedFile.getFile());
        } catch (final InvalidPathException e) {
            context.reporter().error(getClass(),
                "Can't extract meta data from file %s! Reason was: %s", hashedFile.getFile(), e.getMessage());
            return;
        }

        final MediaDataCollector collector = new MediaDataCollector(hashedFile)
            .setMetaData(extractor.extract(absoluteFile.toString()));

        context.emitter().emmit(new Event(EventType.EXTRACT_KEYWORDS_FROM_FILE_NAME, collector));
    }
}
