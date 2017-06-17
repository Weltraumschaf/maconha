package de.weltraumschaf.maconha.core;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link NotFound}.
 */
public final class NotFoundTest {

    @Test
    public void constructorFormatsMessage() {
        final NotFound sut = new NotFound("%s bar %d!", "foo", 23);

        assertThat(sut.getMessage(), is("foo bar 23!"));
    }
}
