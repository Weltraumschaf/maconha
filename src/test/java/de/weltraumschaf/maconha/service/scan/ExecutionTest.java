package de.weltraumschaf.maconha.service.scan;

import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.model.Bucket;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link Execution}.
 */
public final class ExecutionTest {

    private final Execution sut = new Execution(42L, new Bucket(), mock(UI.class));

    @Test
    public void hasStartTime_falseByDefault() {
        assertThat(sut.hasStartTime(), is(false));
    }

    @Test
    public void hasStartTime_trueAfterStart() {
        sut.start();

        assertThat(sut.hasStartTime(), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void start_throwsExceptionIfAlreadyStarted() {
        sut.start();
        sut.start();
    }

    @Test(expected = IllegalStateException.class)
    public void getStartTime_throwsExceptionIfNotStartedYet() {
        sut.getStartTime();
    }

    @Test
    public void getStartTime() {
        sut.start();

        assertThat(sut.getStartTime(), is(not(nullValue())));
    }

    @Test
    public void hasStopTime_falseByDefault() {
        assertThat(sut.hasStopTime(), is(false));
    }

    @Test
    public void hasStopTime_trueAfterStart() {
        sut.stop();

        assertThat(sut.hasStopTime(), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void stop_throwsExceptionIfAlreadyStopped() {
        sut.stop();
        sut.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void getStopTime_throwsExceptionIfNotStartedYet() {
        sut.getStopTime();
    }

    @Test
    public void getStopTime() {
        sut.stop();

        assertThat(sut.getStopTime(), is(not(nullValue())));
    }

}