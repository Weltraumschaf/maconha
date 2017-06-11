package de.weltraumschaf.maconha.service.scanstatus;

import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.service.ScanService;
import org.junit.Before;
import org.junit.Ignore;
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
            "status",
            "type"
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
            "status",
            "type"
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
        final ScanService.ScanStatus strored = new ScanService.ScanStatus(
            42L,
            "bucket",
            "created",
            "started",
            "ended",
            "duration",
            "status",
            "type"
        );
        //noinspection ArraysAsListWithZeroOrOneArgument
        when(serializer.deserialize(any(Reader.class))).thenReturn(Arrays.asList(strored));
        final ScanService.ScanStatus newStatus = new ScanService.ScanStatus(
            43L,
            "bucket",
            "created",
            "started",
            "ended",
            "duration",
            "status",
            "type"
        );

        sut.storeStatus(newStatus);

        //noinspection ArraysAsListWithZeroOrOneArgument
        verify(serializer, times(1))
            .serialize(eq(Arrays.asList(strored, newStatus)), any(Appendable.class));
    }
}
