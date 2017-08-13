package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.MediaFileService;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import org.junit.Test;

import java.util.HashMap;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link FilterSeenFileHandler}.
 */
public final class FilterSeenFileHandlerTest {
    private final EventEmitter emitter = mock(EventEmitter.class);
    private final EventContext context = new EventContext(emitter, new HashMap<>());
    private final MediaFileService mediaFiles = mock(MediaFileService.class);
    private final FilterSeenFileHandler sut = new FilterSeenFileHandler(mediaFiles);

    @Test(expected = NullPointerException.class)
    public void process_contextIsNull() {
        sut.process(null, new Event(EventType.FILTER_SEEN_HASHED_FILE, "data"));
    }

    @Test(expected = NullPointerException.class)
    public void process_eventIsNull() {
        sut.process(context, null);
    }

    @Test(expected = IllegalEventData.class)
    public void process_eventDataIsNotOfTypeBucket() {
        sut.process(context, new Event(EventType.FILTER_SEEN_HASHED_FILE, new Object()));
    }

    @Test(expected = IllegalGlobalData.class)
    public void process_globalDataIsNull() {
        sut.process(
            context,
            new Event(EventType.FILTER_SEEN_HASHED_FILE,
                new HashedFile("hash", "file")));
    }

    @Test(expected = IllegalGlobalData.class)
    public void process_globalDataIsOfWongType() {
        context.globals().put(Global.BUCKET, new Object());

        sut.process(
            context,
            new Event(EventType.FILTER_SEEN_HASHED_FILE,
                new HashedFile("hash", "file")));
    }

    @Test
    public void process_emitEventForUnseenFile() {
        final Bucket bucket = new Bucket();
        context.globals().put(Global.BUCKET, bucket);
        final HashedFile hashedFile = new HashedFile("hashOne", "fileOne");
        when(mediaFiles.isFileUnseen(hashedFile, bucket)).thenReturn(true);

        sut.process(context, new Event(EventType.FILTER_SEEN_HASHED_FILE, hashedFile));

        verify(emitter, times(1))
            .emmit(new Event(EventType.EXTRACT_FILE_META_DATA, hashedFile));
    }

    @Test
    public void process_emitEventForSeenFile() {
        final Bucket bucket = new Bucket();
        context.globals().put(Global.BUCKET, bucket);
        final HashedFile hashedFile = new HashedFile("hashOne", "fileOne");
        when(mediaFiles.isFileUnseen(hashedFile, bucket)).thenReturn(false);

        sut.process(context, new Event(EventType.FILTER_SEEN_HASHED_FILE, hashedFile));

        verify(emitter, never()).emmit(any(Event.class));
    }
}
