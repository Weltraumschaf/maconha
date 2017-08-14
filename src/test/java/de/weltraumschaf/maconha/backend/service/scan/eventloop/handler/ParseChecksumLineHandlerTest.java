package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import org.junit.Test;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link ParseChecksumLineHandler}.
 */
public final class ParseChecksumLineHandlerTest {
    private final EventEmitter emitter = mock(EventEmitter.class);
    private final EventContext context = new EventContext(emitter, new HashMap<>());
    private final ParseChecksumLineHandler sut = new ParseChecksumLineHandler();

    @Test(expected = NullPointerException.class)
    public void process_contextIsNull() {
        sut.process(null, new Event(EventType.PARSE_CHECKSUM_LINE, "data"));
    }

    @Test(expected = NullPointerException.class)
    public void process_eventIsNull() {
        sut.process(context, null);
    }

    @Test(expected = IllegalEventData.class)
    public void process_eventDataIsNotOfTypeBucket() {
        sut.process(context, new Event(EventType.PARSE_CHECKSUM_LINE, new Object()));
    }

    @Test
    public void process_emitsEventWithParsedFileAsPayload() {
        final String data = "2f04335a0b0a96b50f9e19de9fa5a4aac2c0cc42e474bf3b9401790e33e166cb  Animation/android_207_HQ.mp4";

        sut.process(context, new Event(EventType.PARSE_CHECKSUM_LINE, data));

        verify(emitter, times(1))
            .emmit(new Event(
                EventType.FILTER_FILE_EXTENSION,
                new HashedFile("2f04335a0b0a96b50f9e19de9fa5a4aac2c0cc42e474bf3b9401790e33e166cb", "Animation/android_207_HQ.mp4")));
    }
}
