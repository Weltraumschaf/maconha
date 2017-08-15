package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.service.mediafile.KeywordExtractor;
import de.weltraumschaf.maconha.backend.service.mediafile.KeywordsFromFileNameExtractor;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.Event;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventContext;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventHandler;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventType;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;

import java.util.Collection;

/**
 * Handles the event for extracting keywords from a {@link HashedFile#getFile() file name}.
 * <p>
 * Expects a {@link MediaDataCollector hashed file} as {@link Event#getData() event data}.
 * </p>
 */
public final class ExtractKeywordsFromFileNameHandler extends BaseHandler implements EventHandler, HasLogger {
    private final KeywordExtractor extractor;

    public ExtractKeywordsFromFileNameHandler() {
        this(new KeywordsFromFileNameExtractor());
    }

    ExtractKeywordsFromFileNameHandler(final KeywordExtractor extractor) {
        super();
        this.extractor = Validate.notNull(extractor, "extractor");
    }

    @Override
    public void process(final EventContext context, final Event event) {
        assertPreConditions(context, event, MediaDataCollector.class);

        final MediaDataCollector collector = (MediaDataCollector) event.getData();
        final Collection<String> keywords = extractor.extract(collector.getFile().getFile());

        context.emitter()
            .emmit(new Event(EventType.EXTRACT_KEYWORDS_FROM_META_DATA, collector.addKeyWords(keywords)));
    }
}
