
package de.weltraumschaf.maconha.core;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link Movies}.
 */
public class MoviesTest {

    @Test(expected = IllegalArgumentException.class)
    public void forValue_unknown() {
        Movies.forValue("snafu");
    }

    @Test
    public void forValue() {
        assertThat(Movies.forValue(Movies.APPLE_QUICKTIME_MOVIE.getExtension()), is(Movies.APPLE_QUICKTIME_MOVIE));
    }
}
