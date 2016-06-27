
package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import java.util.Collections;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link DefaultSearchService}.
 */
public class DefaultSearchServiceTest {

    private final KeywordRepo keywords = mock(KeywordRepo.class);
    private final DefaultSearchService sut = new DefaultSearchService(keywords);

    @Test
    public void search_nullAsQury() {
        assertThat(sut.search(null), hasSize(0));
    }

    @Test
    public void search_emptyAsQuery() {
        assertThat(sut.search(""), hasSize(0));
        assertThat(sut.search("  \t \n  "), hasSize(0));
    }

    @Test
    public void search_nothingFound() {
        when(keywords.findByLiterals(Matchers.anyCollection())).thenReturn(Collections.emptyList());

        assertThat(sut.search("snafu"), hasSize(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void search_returnsUnmodifiable() {
        sut.search("snafu").add(new Media());
    }

    @Test
    public void splitQuery() {
        assertThat(sut.splitQuery("  foo    bar baz"), contains("foo", "bar", "baz"));
    }
}
