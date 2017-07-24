package de.weltraumschaf.maconha.backend.service.scan;

import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.ScanService;
import de.weltraumschaf.maconha.backend.service.ScanStatusService;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link BaseScanService}.
 */
public final class BaseScanServiceTest {
    private final BaseScanService sut = new BaseScanService(mock(ScanStatusService.class)) {
    };

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
        assertThat(
            sut.formatDateTime(dateTime.toDate()),
            is("12:30:00 14.07.2017"));
    }

    @Test(expected = ScanService.ScanError.class)
    public void getExecution_noJobWithId() {
        sut.getExecution(42L);
    }

    @Test
    public void getExecution() {
        final Execution execution = new Execution(42L, new Bucket(), mock(UI.class));
        sut.scans.put(42L, execution);

        assertThat(sut.getExecution(42L), is(execution));
    }
}
