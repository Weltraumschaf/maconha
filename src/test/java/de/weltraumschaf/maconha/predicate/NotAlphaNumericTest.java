package de.weltraumschaf.maconha.predicate;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link NotAlphaNumeric}.
 */
public final class NotAlphaNumericTest {
    private final NotAlphaNumeric sut = new NotAlphaNumeric();

    @Test
    public void test() {
        assertThat(sut.test(null), is(true));
        assertThat(sut.test(""), is(true));
        assertThat(sut.test("    "), is(true));

        assertThat(sut.test("foo+bar"), is(true));

        assertThat(sut.test("foobar"), is(false));
    }
}
