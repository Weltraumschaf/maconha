package de.weltraumschaf.maconha.backend.service.scan.eventloop;

import de.weltraumschaf.maconha.app.Application;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.MediaFileService;
import de.weltraumschaf.maconha.backend.service.SearchService;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.handler.*;
import de.weltraumschaf.maconha.backend.service.scan.shell.CommandFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
public final class EventLoopIT {

    @Autowired
    private MediaFileService mediaFiles;

    @Test
    @Ignore
    public void experiment() {
        final EventLoop loop = new EventLoop();
        loop.register(EventType.DIR_HASH, new DirHashHandler(mock(CommandFactory.class)));
        loop.register(EventType.LOAD_FILE_HASHES, new LoadFileHashesHandler());
        loop.register(EventType.SPLIT_CHECKSUM_LINES, new SplitChecksumLinesHandler());
        loop.register(EventType.PARSE_CHECKSUM_LINE, new ParseChecksumLineHandler());
        loop.register(EventType.FILTER_FILE_EXTENSION, new FilterFileExtensionHandler());
        loop.register(EventType.RELATIVIZE_HASHED_FILE, new RelativizeFileHandler());
        loop.register(EventType.FILTER_SEEN_HASHED_FILE, new FilterSeenFileHandler(mediaFiles));
        loop.register(EventType.EXTRACT_FILE_META_DATA, new ExtractFileMetaDataHandler());
        loop.register(EventType.EXTRACT_KEYWORDS_FROM_FILE_NAME, new ExtractKeywordsFromFileNameHandler());
        loop.register(EventType.EXTRACT_KEYWORDS_FROM_META_DATA, new ExtractKeywordsFromMetaDataHandler());
        loop.register(EventType.FILTER_UNWANTED_KEYWORDS, new FilterUnwantedKeywords());
        loop.register(EventType.STORE_FILE_AND_KEYWORDS, new StoreFileAndKeywordsHandler(mediaFiles));

        final Bucket bucket = new Bucket();
        bucket.setName("foo");
        bucket.setDirectory("/buckets/foo");

        loop.start(new Event(EventType.DIR_HASH, bucket));
    }

}
