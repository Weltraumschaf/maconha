package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import de.weltraumschaf.maconha.backend.service.scan.reporting.Reporter;
import org.junit.Test;

import java.util.HashMap;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link FilterFileExtensionHandler}.
 */
public final class FilterFileExtensionHandlerTest {
    private final EventEmitter emitter = mock(EventEmitter.class);
    private final EventContext context = new EventContext(emitter, new HashMap<>(), new Reporter());
    private final FilterFileExtensionHandler sut = new FilterFileExtensionHandler();

    @Test(expected = NullPointerException.class)
    public void process_contextIsNull() {
        sut.process(null, new Event(EventType.FILTER_FILE_EXTENSION, "data"));
    }

    @Test(expected = NullPointerException.class)
    public void process_eventIsNull() {
        sut.process(context, null);
    }

    @Test(expected = IllegalEventData.class)
    public void process_eventDataIsNotOfTypeBucket() {
        sut.process(context, new Event(EventType.FILTER_FILE_EXTENSION, new Object()));
    }

    @Test
    public void process_emitEventForWantedExtension() {
        final HashedFile hashedFile = new HashedFile("hash", "file.mpg");

        sut.process(context, new Event(EventType.FILTER_FILE_EXTENSION, hashedFile));

        verify(emitter, times(1)).emmit(new Event(EventType.RELATIVIZE_HASHED_FILE, hashedFile));
    }

    @Test
    public void process_dontEmitEventForUnwantedExtension() {
        sut.process(context, new Event(EventType.FILTER_FILE_EXTENSION, new HashedFile("hash", "file.foo")));

        verify(emitter, never()).emmit(any(Event.class));
    }
}
