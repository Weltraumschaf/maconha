package de.weltraumschaf.maconha.backend.service.mediafile;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link IgnoredKeywords}.
 */
public final class IgnoredKeywordsTest {

    private final IgnoredKeywords sut = new IgnoredKeywords();

    @Test
    public void test_falseForIgnored() {
        for (final String ignoredKeyword : IgnoredKeywords.IGNORED_KEYWORDS) {
            assertThat(
                "Should be false for keyword '" + ignoredKeyword + "'!",
                sut.test(ignoredKeyword), is(false));
        }
    }

    @Test
    public void test_trueForNotIgnored() {
        assertThat(sut.test("foo"), is(true));
        assertThat(sut.test("bar"), is(true));
        assertThat(sut.test("snafu"), is(true));
    }

    @Test
    public void test_falseForNullAsKeyWord() {
        assertThat(sut.test(null), is(false));
    }

    @Test
    public void test_falseForBlankAsKeyword() {
        assertThat(sut.test(""), is(false));
        assertThat(sut.test("   "), is(false));
    }
}
