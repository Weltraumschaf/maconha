package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.FileMetaData;
import de.weltraumschaf.maconha.backend.service.mediafile.KeywordExtractor;
import de.weltraumschaf.maconha.backend.service.mediafile.KeywordsFromMetaDataExtractor;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.Event;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventContext;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventHandler;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventType;

import java.util.Collection;

/**
 * Handles the event for extracting keywords from {@link FileMetaData file meta data}.
 * <p>
 * Expects a {@link MediaDataCollector hashed file} as {@link Event#getData() event data}.
 * </p>
 */
public final class ExtractKeywordsFromMetaDataHandler extends BaseHandler implements EventHandler, HasLogger {
    private final KeywordExtractor extractor;

    public ExtractKeywordsFromMetaDataHandler() {
        this(new KeywordsFromMetaDataExtractor());
    }

    ExtractKeywordsFromMetaDataHandler(final KeywordExtractor extractor) {
        super();
        this.extractor = Validate.notNull(extractor, "extractor");
    }

    @Override
    public void process(final EventContext context, final Event event) {
        assertPreConditions(context, event, MediaDataCollector.class);
        final MediaDataCollector collector = (MediaDataCollector) event.getData();
        final Collection<String> keywords = extractor.extract(collector.getMetaData().getData());

        context.emitter()
            .emmit(new Event(EventType.STORE_FILE_AND_KEYWORDS, collector.addKeyWords(keywords)));
    }
}
