package de.weltraumschaf.maconha.backend.service.scanstatus;

import de.weltraumschaf.maconha.app.MaconhaConfiguration;
import de.weltraumschaf.maconha.backend.service.ScanService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link DefaultScanStatusService}.
 */
public final class DefaultScanStatusServiceTest {
    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    private final MaconhaConfiguration config = new MaconhaConfiguration();
    private final StatusSerializer serializer = mock(StatusSerializer.class);
    private final DefaultScanStatusService sut = new DefaultScanStatusService(config, serializer);

    private void createStatusesFile() throws IOException {
        final File folder = tmp.newFolder(DefaultScanStatusService.STATUSES_DIR_NAME);
        //noinspection ResultOfMethodCallIgnored
        new File(folder, DefaultScanStatusService.STATUSES_FILE_NAME).createNewFile();
    }

    @Before
    public void setDirecotryAtConfig() {
        config.setHomedir(tmp.getRoot().getAbsolutePath());
    }

    @Test
    public void readStatusFile_fileDoesNotExists() {
        assertThat(sut.allStatuses(), is(empty()));
    }

    @Test
    public void readStatusFile() throws IOException {
        createStatusesFile();
        final ScanService.ScanStatus status = new ScanService.ScanStatus(
            42L,
            "bucket",
            "created",
            "started",
            "ended",
            "duration",
            "status"
        );
        when(serializer.deserialize(any(Reader.class))).thenReturn(Collections.singletonList(status));

        assertThat(sut.allStatuses(), hasSize(1));
        assertThat(sut.allStatuses(), contains(status));
    }


    @Test
    public void storeStatusesToFile_fileDoesNotExists() {
        final ScanService.ScanStatus status = new ScanService.ScanStatus(
            42L,
            "bucket",
            "created",
            "started",
            "ended",
            "duration",
            "status"
        );

        sut.storeStatus(status);

        assertThat(
            "Directory was not created!",
            Files.isDirectory(Paths.get(config.getHomedir()).resolve(DefaultScanStatusService.STATUSES_DIR_NAME)),
            is(true));
        //noinspection ArraysAsListWithZeroOrOneArgument
        verify(serializer, times(1))
            .serialize(eq(Arrays.asList(status)), any(Appendable.class));
    }

    @Test
    public void storeStatusesToFile() throws IOException {
        createStatusesFile();
        final ScanService.ScanStatus stored = new ScanService.ScanStatus(
            42L,
            "bucket",
            "created",
            "started",
            "ended",
            "duration",
            "status"
        );
        when(serializer.deserialize(any(Reader.class))).thenReturn(Collections.singletonList(stored));
        final ScanService.ScanStatus newStatus = new ScanService.ScanStatus(
            43L,
            "bucket",
            "created",
            "started",
            "ended",
            "duration",
            "status"
        );

        sut.storeStatus(newStatus);

        verify(serializer, times(1))
            .serialize(eq(Arrays.asList(stored, newStatus)), any(Appendable.class));
    }

    @Test
    public void nextId_noStatusesStoredYet() throws IOException {
        createStatusesFile();
        when(serializer.deserialize(any(Reader.class))).thenReturn(Collections.emptyList());

        assertThat(sut.nextId(), is(1L));
    }

    @Test
    public void nextId() throws IOException {
        createStatusesFile();
        final ScanService.ScanStatus one = new ScanService.ScanStatus(
            42L,
            "bucket",
            "created",
            "started",
            "ended",
            "duration",
            "status"
        );
        final ScanService.ScanStatus two = new ScanService.ScanStatus(
            23L,
            "bucket",
            "created",
            "started",
            "ended",
            "duration",
            "status"
        );
        when(serializer.deserialize(any(Reader.class))).thenReturn(Arrays.asList(one, two));

        assertThat(sut.nextId(), is(43L));
    }
}
