package de.weltraumschaf.maconha.job;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link ProgressMonitor}.
 */
public class ProgressMonitorTest {

    private static final double DELTA = 0.001;
    private final ProgressMonitor sut = new ProgressMonitor();

    @Test
    public void progress_byDefaultisZero() {
        assertThat(sut.progress(), is(0.));
    }

    @Test
    public void progress() {
        sut.begin(6);
        assertThat(sut.progress(), is(0.));

        sut.worked(1);
        assertThat(sut.progress(), closeTo(.16666666667, DELTA));

        sut.worked(1);
        assertThat(sut.progress(), closeTo(.33333333333, DELTA));

        sut.worked(1);
        assertThat(sut.progress(), closeTo(.5, DELTA));

        sut.worked(1);
        assertThat(sut.progress(), closeTo(.66666666667, DELTA));

        sut.worked(1);
        assertThat(sut.progress(), closeTo(.83333333333, DELTA));

        sut.worked(1);
        assertThat(sut.progress(), closeTo(1., DELTA));
    }

    @Test
    public void done() {
        sut.begin(100);

        sut.done();

        assertThat(sut.progress(), is(1.));
    }

    @Test(expected = IllegalArgumentException.class)
    public void begin_setTooLessToralWork() {
        sut.begin(0);
    }

    @Test
    public void worked() {
        sut.begin(1);

        sut.worked(0);

        assertThat(sut.progress(), is(0.));
    }

}
