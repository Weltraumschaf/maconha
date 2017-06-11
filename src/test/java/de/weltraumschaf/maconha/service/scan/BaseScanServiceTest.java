package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.maconha.service.ScanStatusService;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Tests for {@link BaseScanService}.
 */
public final class BaseScanServiceTest {
    private final BaseScanService sut = new BaseScanService(mock(ScanStatusService.class)) {
    };

    @Test
    @Ignore
    public void todo() {
    }
}
