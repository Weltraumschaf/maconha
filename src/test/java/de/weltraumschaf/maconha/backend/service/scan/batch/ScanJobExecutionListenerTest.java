package de.weltraumschaf.maconha.backend.service.scan.batch;

import de.weltraumschaf.maconha.backend.service.scan.ScanCallBack;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link ScanJobExecutionListener}.
 */
public final class ScanJobExecutionListenerTest {

    private final ScanJobExecutionListener sut = new ScanJobExecutionListener();

    @Test
    public void beforeJob() {
        final ScanCallBack callbackOne = mock(ScanCallBack.class);
        sut.register(callbackOne);
        final ScanCallBack callbackTwo = mock(ScanCallBack.class);
        sut.register(callbackTwo);
        final ScanCallBack callbackThree = mock(ScanCallBack.class);
        sut.register(callbackThree);

        final JobExecution execution = mock(JobExecution.class);
        when(execution.getJobId()).thenReturn(42L);

        sut.beforeJob(execution);

        verify(callbackOne, times(1)).beforeScan(42L);
        verify(callbackTwo, times(1)).beforeScan(42L);
        verify(callbackThree, times(1)).beforeScan(42L);
    }

    @Test
    public void afterJob() {
        final ScanCallBack callbackOne = mock(ScanCallBack.class);
        sut.register(callbackOne);
        final ScanCallBack callbackTwo = mock(ScanCallBack.class);
        sut.register(callbackTwo);
        final ScanCallBack callbackThree = mock(ScanCallBack.class);
        sut.register(callbackThree);

        final JobExecution execution = mock(JobExecution.class);
        when(execution.getJobId()).thenReturn(42L);

        sut.afterJob(execution);

        verify(callbackOne, times(1)).afterScan(42L);
        verify(callbackTwo, times(1)).afterScan(42L);
        verify(callbackThree, times(1)).afterScan(42L);
    }
}
