package de.weltraumschaf.maconha.ui.controller;

import de.weltraumschaf.maconha.backend.model.MediaType;
import de.weltraumschaf.maconha.backend.service.SearchService;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SearchController}.
 */
public final class SearchControllerTest {

    private final SearchController sut = new SearchController(mock(SearchService.class));

    @Test
    public void sanitizeQuery_nullAsQuery() {
        assertThat(sut.sanitizeQuery(null), is(Collections.emptyList()));
    }

    @Test
    public void sanitizeQuery_emptyAsQuery() {
        assertThat(sut.sanitizeQuery(""), is(Collections.emptyList()));
    }

    @Test
    public void sanitizeQuery_blankAsQuery() {
        assertThat(sut.sanitizeQuery("  \n    \t  "), is(Collections.emptyList()));
    }

    @Test
    public void sanitizeQuery() {
        assertThat(sut.sanitizeQuery(" foo  bar     baz"), is(Arrays.asList("foo", "bar", "baz")));
    }

    @Test
    public void sanitizeTypes() {
        assertThat(sut.sanitizeTypes(null), is(EnumSet.allOf(MediaType.class)));
        assertThat(sut.sanitizeTypes(Collections.emptyList()), is(EnumSet.allOf(MediaType.class)));

        assertThat(sut.sanitizeTypes(Collections.singleton("all")), is(EnumSet.allOf(MediaType.class)));
        assertThat(sut.sanitizeTypes(Arrays.asList("video", "audio")), is(Arrays.asList(MediaType.VIDEO, MediaType.AUDIO)));
        assertThat(sut.sanitizeTypes(Arrays.asList("video", "all",  "audio")), is(EnumSet.allOf(MediaType.class)));
    }
}
