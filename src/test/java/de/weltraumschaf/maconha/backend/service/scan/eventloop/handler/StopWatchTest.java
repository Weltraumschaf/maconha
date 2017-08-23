package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import org.junit.Test;

import static org.junit.Assert.*;
import static de.weltraumschaf.commons.testing.hamcrest.CommonsTestingMatchers.closeTo;

/**
 * Tests for  {@link StopWatch}.
 */
public class StopWatchTest {
    private final StopWatch sut = new StopWatch();

    @Test(expected = IllegalStateException.class)
    public void start_cantStartTwice() {
        sut.start();
        sut.start();
    }

    @Test(expected = IllegalStateException.class)
    public void stop_cantStopTwice() {
        sut.stop();
        sut.stop();
    }

    @Test(expected = IllegalStateException.class)
    public void duration_notStartetdYet() {
        sut.duration();
    }

    @Test(expected = IllegalStateException.class)
    public void duration_notStoppedYet() {
        sut.start();
        sut.duration();
    }

    @Test
    public void duration() throws InterruptedException {
        sut.start();
        Thread.sleep(50);
        sut.stop();

        assertThat(sut.duration(), closeTo(50L, 5L));
    }
}