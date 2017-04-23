
package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.maconha.service.scan.MalformedKeywords;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link MalformedKeywords}.
 */
public class MalformedKeywordsTest {

    private final MalformedKeywords sut = new MalformedKeywords();

    @Test
    public void accept_nullAndEmptyNotAccepted() {
        assertThat(sut.test(null), is(false));
        assertThat(sut.test(""), is(false));
        assertThat(sut.test("   "), is(false));
        assertThat(sut.test("  \t \n "), is(false));
    }

    @Test
    public void accept_true() {
        assertThat(sut.test("13te"), is(true));
        assertThat(sut.test("1"), is(true));
        assertThat(sut.test("13"), is(true));
    }

    @Test
    public void accept_false() {
        assertThat(sut.test("'s"), is(false));
        assertThat(sut.test("("), is(false));
        assertThat(sut.test("(s"), is(false));
        assertThat(sut.test(")"), is(false));
        assertThat(sut.test("+7"), is(false));
        assertThat(sut.test(","), is(false));
        assertThat(sut.test("0"), is(false));
        assertThat(sut.test("01"), is(false));
        assertThat(sut.test("0000345"), is(false));
        assertThat(sut.test("a"), is(false));
        assertThat(sut.test("ab"), is(false));
        assertThat(sut.test("2v"), is(false));
    }
}
