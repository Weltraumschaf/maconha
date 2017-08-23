package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.backend.model.FileMetaData;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.mediafile.Extractor;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import de.weltraumschaf.maconha.backend.service.scan.reporting.Reporter;
import org.junit.Test;

import java.util.HashMap;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link ExtractFileMetaDataHandler}.
 */
public final class ExtractFileMetaDataHandlerTest {
    private final EventEmitter emitter = mock(EventEmitter.class);
    private final EventContext context = new EventContext(emitter, new HashMap<>(), new Reporter());
    @SuppressWarnings("unchecked")
    private Extractor<FileMetaData> extractor = mock(Extractor.class);
    private final ExtractFileMetaDataHandler sut = new ExtractFileMetaDataHandler(extractor);

    @Test(expected = NullPointerException.class)
    public void process_contextIsNull() {
        sut.process(null, new Event(EventType.EXTRACT_FILE_META_DATA, "data"));
    }

    @Test(expected = NullPointerException.class)
    public void process_eventIsNull() {
        sut.process(context, null);
    }

    @Test(expected = IllegalEventData.class)
    public void process_eventDataIsNotOfTypeBucket() {
        sut.process(context, new Event(EventType.EXTRACT_FILE_META_DATA, new Object()));
    }

    @Test(expected = IllegalGlobalData.class)
    public void process_globalDataIsNull() {
        sut.process(
            context,
            new Event(EventType.EXTRACT_FILE_META_DATA,
                new HashedFile("hash", "file")));
    }

    @Test(expected = IllegalGlobalData.class)
    public void process_globalDataIsOfWongType() {
        context.globals().put(Global.BUCKET, new Object());

        sut.process(
            context,
            new Event(EventType.EXTRACT_FILE_META_DATA,
                new HashedFile("hash", "file")));
    }

    @Test
    public void process_emitEventWithCollector() {
        final Bucket bucket = new Bucket();
        bucket.setDirectory("/foo/bar");
        context.globals().put(Global.BUCKET, bucket);
        final FileMetaData metaData = new FileMetaData("mime", "data");
        when(extractor.extract("/foo/bar/file")).thenReturn(metaData);
        final HashedFile hashedFile = new HashedFile("hash", "file");

        sut.process(
            context,
            new Event(EventType.EXTRACT_FILE_META_DATA,
                hashedFile));

        verify(emitter, times(1))
            .emmit(new Event(
                EventType.EXTRACT_KEYWORDS_FROM_FILE_NAME,
                new MediaDataCollector(hashedFile).setMetaData(metaData)));
    }
}
