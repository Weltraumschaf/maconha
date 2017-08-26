package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.Reporter;
import org.junit.Test;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link RelativizeFileHandler}.
 */
public final class RelativizeFileHandlerTest {
    private final EventEmitter emitter = mock(EventEmitter.class);
    private final EventContext context = new EventContext(emitter, new HashMap<>(), new Reporter());
    private final RelativizeFileHandler sut = new RelativizeFileHandler();

    @Test(expected = NullPointerException.class)
    public void process_contextIsNull() {
        sut.process(null, new Event(EventType.RELATIVIZE_HASHED_FILE, "data"));
    }

    @Test(expected = NullPointerException.class)
    public void process_eventIsNull() {
        sut.process(context, null);
    }

    @Test(expected = IllegalEventData.class)
    public void process_eventDataIsNotOfRightType() {
        sut.process(context, new Event(EventType.RELATIVIZE_HASHED_FILE, new Object()));
    }

    @Test(expected = IllegalGlobalData.class)
    public void process_globalDataIsNull() {
        sut.process(
            context,
            new Event(EventType.RELATIVIZE_HASHED_FILE,
                new HashedFile("hash", "file")));
    }

    @Test(expected = IllegalGlobalData.class)
    public void process_globalDataIsOfWongType() {
        context.globals().put(Global.BUCKET, new Object());

        sut.process(
            context,
            new Event(EventType.RELATIVIZE_HASHED_FILE,
                new HashedFile("hash", "file")));
    }

    @Test
    public void process_emitEventWithRelativeHashedFile() {
        final Bucket bucket = new Bucket();
        bucket.setDirectory("/foo/bar");
        context.globals().put(Global.BUCKET, bucket);

        sut.process(
            context,
            new Event(EventType.RELATIVIZE_HASHED_FILE,
                new HashedFile("hash", "/foo/bar/baz/file")));

        verify(emitter, times(1))
            .emmit(new Event(
                EventType.FILTER_SEEN_HASHED_FILE,
                new HashedFile("hash", "baz/file")));
    }
}
