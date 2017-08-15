package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.backend.service.mediafile.KeywordExtractor;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link ExtractKeywordsFromFileNameHandler}.
 */
public final class ExtractKeywordsFromFileNameHandlerTest {
    private final EventEmitter emitter = mock(EventEmitter.class);
    private final EventContext context = new EventContext(emitter, new HashMap<>());
    private KeywordExtractor extractor = mock(KeywordExtractor.class);
    private final ExtractKeywordsFromFileNameHandler sut = new ExtractKeywordsFromFileNameHandler(extractor);

    @Test(expected = NullPointerException.class)
    public void process_contextIsNull() {
        sut.process(null, new Event(EventType.EXTRACT_KEYWORDS_FROM_FILE_NAME, "data"));
    }

    @Test(expected = NullPointerException.class)
    public void process_eventIsNull() {
        sut.process(context, null);
    }

    @Test(expected = IllegalEventData.class)
    public void process_eventDataIsNotOfTypeBucket() {
        sut.process(context, new Event(EventType.EXTRACT_KEYWORDS_FROM_FILE_NAME, new Object()));
    }

    @Test
    public void process_emitEventWithKeywords() {
        final MediaDataCollector input = new MediaDataCollector(new HashedFile("hash", "file"));
        when(extractor.extract("file")).thenReturn(Arrays.asList("k1", "k2"));

        sut.process(context, new Event(EventType.EXTRACT_KEYWORDS_FROM_META_DATA, input));

        verify(emitter, times(1))
            .emmit(new Event(
                EventType.EXTRACT_KEYWORDS_FROM_META_DATA,
                input.addKeyWords(Arrays.asList("k1", "k2"))));
    }
}
