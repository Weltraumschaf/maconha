package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.Reporter;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link FilterUnwantedKeywords}.
 */
public final class FilterUnwantedKeywordsTest {
    private final EventEmitter emitter = mock(EventEmitter.class);
    private final EventContext context = new EventContext(emitter, new HashMap<>(), new Reporter());
    @SuppressWarnings("unchecked")
    private Predicate<String> filterOne = mock(Predicate.class);
    @SuppressWarnings("unchecked")
    private Predicate<String> filterTwo = mock(Predicate.class);
    private final FilterUnwantedKeywords sut = new FilterUnwantedKeywords(Arrays.asList(filterOne, filterTwo));

    @Test(expected = NullPointerException.class)
    public void process_contextIsNull() {
        sut.process(null, new Event(EventType.FILTER_UNWANTED_KEYWORDS, "data"));
    }

    @Test(expected = NullPointerException.class)
    public void process_eventIsNull() {
        sut.process(context, null);
    }

    @Test(expected = IllegalEventData.class)
    public void process_eventDataIsNotOfTypeBucket() {
        sut.process(context, new Event(EventType.FILTER_UNWANTED_KEYWORDS, new Object()));
    }

    @Test
    public void process_emitEventWithFilteredKeywords() {
        when(filterOne.test("k1")).thenReturn(true);
        when(filterOne.test("k3")).thenReturn(true);
        when(filterTwo.test("k1")).thenReturn(true);
        when(filterTwo.test("k3")).thenReturn(true);
        final HashedFile hashedFile = new HashedFile("hash", "file");

        sut.process(
            context,
            new Event(
                EventType.FILTER_UNWANTED_KEYWORDS,
                new MediaDataCollector(hashedFile)
                    .addKeyWords(Arrays.asList("k1", "k2", "k3" ,"4"))));

        verify(emitter, times(1))
            .emmit(new Event(
                EventType.STORE_FILE_AND_KEYWORDS,
                new MediaDataCollector(hashedFile).setKeyWords(Arrays.asList("k1", "k3"))));
    }
}
