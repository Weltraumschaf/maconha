package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.ScanService;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.*;
import de.weltraumschaf.maconha.backend.service.scan.reporting.Reporter;
import de.weltraumschaf.maconha.backend.service.scan.shell.Command;
import de.weltraumschaf.maconha.backend.service.scan.shell.CommandFactory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link DirHashHandler}.
 */
public final class DirHashHandlerTest {

    private final EventEmitter emitter = mock(EventEmitter.class);
    private final EventContext context = new EventContext(emitter, new HashMap<>(), new Reporter());
    private final CommandFactory cmds = mock(CommandFactory.class);
    private final DirHashHandler sut = new DirHashHandler(cmds);

    @Test(expected = NullPointerException.class)
    public void process_contextIsNull() {
        sut.process(null, new Event(EventType.DIR_HASH, "data"));
    }

    @Test(expected = NullPointerException.class)
    public void process_eventIsNull() {
        sut.process(context, null);
    }

    @Test(expected = IllegalEventData.class)
    public void process_eventDataIsNotOfTypeBucket() {
        sut.process(context, new Event(EventType.DIR_HASH, "data"));
    }

    @Test
    public void process_storesBucketInGlobals() {
        final Bucket data = new Bucket();
        data.setDirectory("/foo/bar");
        when(cmds.dirhash(any(Path.class))).thenReturn(mock(Command.class));

        sut.process(context, new Event(EventType.DIR_HASH, data));

        assertThat(context.globals().get(Global.BUCKET), is(sameInstance(data)));
    }

    @Test
    public void process_emitsLoadFileHashesEvent() {
        final Bucket data = new Bucket();
        data.setDirectory("/foo/bar");
        when(cmds.dirhash(any(Path.class))).thenReturn(mock(Command.class));

        sut.process(context, new Event(EventType.DIR_HASH, data));

        verify(emitter, times(1))
            .emmit(new Event(EventType.LOAD_FILE_HASHES, "/foo/bar/" + ScanService.CHECKSUM_FILE));
    }

    @Test
    public void process_dirhashingBucket() throws IOException, InterruptedException {
        final Bucket data = new Bucket();
        data.setDirectory("/foo/bar");
        final Command dirhash = mock(Command.class);
        when(cmds.dirhash(Paths.get(data.getDirectory()))).thenReturn(dirhash);

        sut.process(context, new Event(EventType.DIR_HASH, data));

        verify(dirhash, times(1)).execute();
    }
}
