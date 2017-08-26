package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.Reporter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link LoadFileHashesHandler}.
 */
public final class LoadFileHashesHandlerTest {
    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();
    private final EventEmitter emitter = mock(EventEmitter.class);
    private final EventContext context = new EventContext(emitter, new HashMap<>(), new Reporter());
    private final LoadFileHashesHandler sut = new LoadFileHashesHandler();

    @Test(expected = NullPointerException.class)
    public void process_contextIsNull() {
        sut.process(null, new Event(EventType.LOAD_FILE_HASHES, "data"));
    }

    @Test(expected = NullPointerException.class)
    public void process_eventIsNull() {
        sut.process(context, null);
    }

    @Test(expected = IllegalEventData.class)
    public void process_eventDataIsNotOfRightType() {
        sut.process(context, new Event(EventType.LOAD_FILE_HASHES, new Object()));
    }

    @Test
    public void process_emitsSplitChecksumLinesEventWithFileContentAsPayload() throws IOException {
        final Path checksumFile = tmp.newFile().toPath();
        Files.write(checksumFile, "file content".getBytes());

        sut.process(context, new Event(EventType.LOAD_FILE_HASHES, checksumFile.toString()));

        verify(emitter, times(1))
            .emmit(new Event(EventType.SPLIT_CHECKSUM_LINES, "file content"));
    }
}
