package de.weltraumschaf.maconha.backend.service.scan;

import de.weltraumschaf.maconha.app.MaconhaConfiguration;
import de.weltraumschaf.maconha.backend.service.MediaFileService;
import de.weltraumschaf.maconha.backend.service.ScanReportService;
import de.weltraumschaf.maconha.backend.service.ScanService;
import de.weltraumschaf.maconha.backend.service.ScanStatusService;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.task.TaskExecutor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ThreadScanService}.
 */
public final class ThreadScanServiceTest {

    private final MaconhaConfiguration config = mock(MaconhaConfiguration.class);
    private final MediaFileService mediaFiles = mock(MediaFileService.class);
    private final TaskExecutor executor = mock(TaskExecutor.class);
    private final ScanStatusService statuses = mock(ScanStatusService.class);
    private final ScanReportService reports = mock(ScanReportService.class);
    private final ThreadScanService sut = new ThreadScanService(config, mediaFiles, executor, statuses, reports);

    @Test
    public void formatDuration() {
        assertThat(
            sut.formatDuration(
                new DateTime(2017, 7, 14, 12, 30),
                new DateTime(2017, 7, 15, 14, 45)),
            is("26:15:00"));
    }

    @Test
    public void formatDateTime() {
        final DateTime dateTime = new DateTime(2017, 7, 14, 12, 30);

        assertThat(
            sut.formatDateTime(dateTime),
            is("12:30:00 14.07.2017"));
    }

    @Test(expected = ScanService.ScanError.class)
    public void getExecution_noJobWithId() {
        sut.getExecution(42L);
    }

    @Test
    @Ignore
    public void getExecution() {
//        final Execution execution = new Execution(42L, new Bucket(), mock(UI.class));
//        sut.scans.put(42L, execution);

//        assertThat(sut.getExecution(42L), is(execution));
    }
}
