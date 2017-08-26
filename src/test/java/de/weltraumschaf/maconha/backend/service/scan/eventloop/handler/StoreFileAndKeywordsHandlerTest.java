package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.backend.model.FileMetaData;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.MediaFileService;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.Reporter;
import org.junit.Test;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link StoreFileAndKeywordsHandler}.
 */
public final class StoreFileAndKeywordsHandlerTest {
    private final EventEmitter emitter = mock(EventEmitter.class);
    private final EventContext context = new EventContext(emitter, new HashMap<>(), new Reporter());
    private final MediaFileService mediaFiles = mock(MediaFileService.class);
    private final StoreFileAndKeywordsHandler sut = new StoreFileAndKeywordsHandler(mediaFiles);

    @Test(expected = NullPointerException.class)
    public void process_contextIsNull() {
        sut.process(null, new Event(EventType.EXTRACT_KEYWORDS_FROM_META_DATA, "data"));
    }

    @Test(expected = NullPointerException.class)
    public void process_eventIsNull() {
        sut.process(context, null);
    }

    @Test(expected = IllegalEventData.class)
    public void process_eventDataIsNotOfRightType() {
        sut.process(context, new Event(EventType.EXTRACT_KEYWORDS_FROM_META_DATA, new Object()));
    }

    @Test
    public void process_storesMetaData() {
        final Bucket bucket = new Bucket();
        bucket.setDirectory("/foo/bar");
        bucket.setName("snafu");
        context.globals().put(Global.BUCKET, bucket);
        final HashedFile hashedFile = new HashedFile("hash", "file");
        final FileMetaData metaData = new FileMetaData("mime", "data");
        final Collection<String> keywords = new HashSet<>(Arrays.asList("k1", "k2", "k3"));
        final MediaDataCollector collector = new MediaDataCollector(hashedFile)
            .setMetaData(metaData)
            .setKeyWords(keywords);

        sut.process(context, new Event(EventType.EXTRACT_KEYWORDS_FROM_META_DATA, collector));

        verify(mediaFiles, times(1))
            .storeMetaData(bucket, hashedFile, metaData.getMime(), keywords);
    }
}
