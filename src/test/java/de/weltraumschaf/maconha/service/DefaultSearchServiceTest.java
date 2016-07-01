package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.service.DefaultSearchService.SotByMatchedKeywords;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
    public void splitQuery() {
        assertThat(sut.splitQuery("  foo    bar baz"), contains("foo", "bar", "baz"));
    }

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
        when(keywords.findByLiteralIn(Matchers.anyCollection())).thenReturn(Collections.emptyList());

        assertThat(sut.search("snafu"), hasSize(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void search_returnsUnmodifiable() {
        sut.search("snafu").add(new Media());
    }

    @Test
    public void search() {
        final Media mediaOne = new Media().setTitle("one");
        final Media mediaTwo = new Media().setTitle("two");
        final Media mediaThree = new Media().setTitle("three");
        final Keyword foo = new Keyword()
            .setLiteral("foo")
            .addMedias(mediaOne)
            .addMedias(mediaThree);
        final Keyword bar = new Keyword()
            .setLiteral("bar")
            .addMedias(mediaOne)
            .addMedias(mediaTwo);
        final Keyword baz = new Keyword()
            .setLiteral("baz")
            .addMedias(mediaOne)
            .addMedias(mediaTwo);
        final Collection<Keyword> found = new ArrayList<>();
        found.add(foo);
        found.add(bar);
        found.add(baz);
        when(keywords.findByLiteralIn(Arrays.asList("foo", "bar", "baz"))).thenReturn(found);

        final Collection<Media> result = sut.search("foo  bar baz");

        assertThat(result, contains(mediaOne, mediaTwo, mediaThree));
    }


    @Test
    public void countContainingKeywords() {
        final Keyword foo = new Keyword().setLiteral("foo");
        final Keyword bar = new Keyword().setLiteral("bar");
        final Keyword baz = new Keyword().setLiteral("baz");
        final SotByMatchedKeywords innerSut = new SotByMatchedKeywords(Arrays.asList(foo, bar, baz));

        assertThat(innerSut.countContainingKeywords(null), is(0));
        assertThat(
            innerSut.countContainingKeywords(
                new Media().addKeyword(new Keyword().setLiteral("snafu"))),
            is(0));
        assertThat(
            innerSut.countContainingKeywords(
                new Media().addKeyword(new Keyword().setLiteral("snafu")).addKeyword(foo)),
            is(1));
        assertThat(
            innerSut.countContainingKeywords(
                new Media().addKeyword(new Keyword().setLiteral("snafu")).addKeyword(foo).addKeyword(bar)),
            is(2));
        assertThat(
            innerSut.countContainingKeywords(
                new Media().addKeyword(new Keyword().setLiteral("snafu")).addKeyword(baz).addKeyword(foo).addKeyword(bar)),
            is(3));
    }
}
