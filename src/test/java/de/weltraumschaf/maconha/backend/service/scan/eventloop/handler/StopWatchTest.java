package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static de.weltraumschaf.commons.testing.hamcrest.CommonsTestingMatchers.closeTo;

/**
 * Tests for  {@link StopWatch}.
 */
public class StopWatchTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private final StopWatch sut = new StopWatch();

    @Test
    public void isStarted_byDefaultFalse() {
        assertThat(sut.isStarted(), is(false));
    }

    @Test
    public void isStarted_trueAfterStart() {
        sut.start();

        assertThat(sut.isStarted(), is(true));
    }

    @Test
    public void isStarted_falseAfterReset() {
        sut.start();

        sut.reset();

        assertThat(sut.isStarted(), is(false));
    }

    @Test
    public void isStopped_byDefaultFalse() {
        assertThat(sut.isStopped(), is(false));
    }

    @Test
    public void isStopped_trueAfterStop() {
        sut.start();
        sut.stop();

        assertThat(sut.isStopped(), is(true));
    }

    @Test
    public void isStopped_falseAfterReset() {
        sut.start();
        sut.stop();

        sut.reset();

        assertThat(sut.isStopped(), is(false));
    }

    @Test
    public void start_cantStartTwice() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Stopwatch already started!");

        sut.start();
        sut.start();
    }

    @Test
    public void stop_cantStopTwice() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Stopwatch already stopped!");

        sut.start();
        sut.stop();
        sut.stop();
    }

    @Test
    public void stop_cantStopWithoutStart() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Stopwatch never started! Start it first.");

        sut.stop();
    }

    @Test
    public void duration_notStartedYet() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Stop watch never started! Start first.");

        sut.duration();
    }

    @Test
    public void duration_notStoppedYet() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Stop watch never stopped! Stop first.");

        sut.start();
        sut.duration();
    }

    @Test
    public void duration() throws InterruptedException {
        sut.start();
        Thread.sleep(100);
        sut.stop();

        assertThat(sut.duration(), closeTo(100L, 10L));
    }
}