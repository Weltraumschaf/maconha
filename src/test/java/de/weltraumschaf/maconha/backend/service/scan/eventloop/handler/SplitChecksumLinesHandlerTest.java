package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.Reporter;
import org.junit.Test;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link SplitChecksumLinesHandler}.
 */
public final class SplitChecksumLinesHandlerTest {
    private final EventEmitter emitter = mock(EventEmitter.class);
    private final EventContext context = new EventContext(emitter, new HashMap<>(), new Reporter());
    private final SplitChecksumLinesHandler sut = new SplitChecksumLinesHandler();

    @Test(expected = NullPointerException.class)
    public void process_contextIsNull() {
        sut.process(null, new Event(EventType.SPLIT_CHECKSUM_LINES, "data"));
    }

    @Test(expected = NullPointerException.class)
    public void process_eventIsNull() {
        sut.process(context, null);
    }

    @Test(expected = IllegalEventData.class)
    public void process_eventDataIsNotOfRightType() {
        sut.process(context, new Event(EventType.SPLIT_CHECKSUM_LINES, new Object()));
    }

    @Test
    public void process_emitEventsForEachLine() {
        final String data = "lineOne\nlineTwo\r\nlineThree\n";

        sut.process(context, new Event(EventType.SPLIT_CHECKSUM_LINES, data));

        verify(emitter, times(1)).emmit(new Event(EventType.PARSE_CHECKSUM_LINE, "lineOne"));
        verify(emitter, times(1)).emmit(new Event(EventType.PARSE_CHECKSUM_LINE, "lineTwo"));
        verify(emitter, times(1)).emmit(new Event(EventType.PARSE_CHECKSUM_LINE, "lineThree"));
    }

}
