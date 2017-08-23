package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.backend.model.FileMetaData;
import de.weltraumschaf.maconha.backend.service.mediafile.KeywordExtractor;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import de.weltraumschaf.maconha.backend.service.scan.reporting.Reporter;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link ExtractKeywordsFromMetaDataHandler}.
 */
public final class ExtractKeywordsFromMetaDataHandlerTest {
    private final EventEmitter emitter = mock(EventEmitter.class);
    private final EventContext context = new EventContext(emitter, new HashMap<>(), new Reporter());
    private KeywordExtractor extractor = mock(KeywordExtractor.class);
    private final ExtractKeywordsFromMetaDataHandler sut = new ExtractKeywordsFromMetaDataHandler(extractor);

    @Test(expected = NullPointerException.class)
    public void process_contextIsNull() {
        sut.process(null, new Event(EventType.EXTRACT_KEYWORDS_FROM_META_DATA, "data"));
    }

    @Test(expected = NullPointerException.class)
    public void process_eventIsNull() {
        sut.process(context, null);
    }

    @Test(expected = IllegalEventData.class)
    public void process_eventDataIsNotOfTypeBucket() {
        sut.process(context, new Event(EventType.EXTRACT_KEYWORDS_FROM_META_DATA, new Object()));
    }

    @Test
    public void process_emitEventWithKeywords() {
        final MediaDataCollector input = new MediaDataCollector(new HashedFile("hash", "file"))
            .setMetaData(new FileMetaData("mime", "data"));
        when(extractor.extract("data")).thenReturn(Arrays.asList("k1", "k2"));

        sut.process(context, new Event(EventType.EXTRACT_KEYWORDS_FROM_META_DATA, input));

        verify(emitter, times(1))
            .emmit(new Event(
                EventType.FILTER_UNWANTED_KEYWORDS,
                input.addKeyWords(Arrays.asList("k1", "k2"))));
    }
}
