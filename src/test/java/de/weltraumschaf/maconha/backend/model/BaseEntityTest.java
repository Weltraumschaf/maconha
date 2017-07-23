package de.weltraumschaf.maconha.backend.model;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link BaseEntity}.
 */
public final class BaseEntityTest {

    private final BaseEntity sut = new BaseEntity() {};

    @Test
    public void sameAsFormer() {
        assertThat(sut.sameAsFormer(null, null), is(true));
        assertThat(sut.sameAsFormer("foo", "foo"), is(true));

        assertThat(sut.sameAsFormer("foo", null), is(false));
        assertThat(sut.sameAsFormer(null, "foo"), is(false));
        assertThat(sut.sameAsFormer("foo", "bar"), is(false));
    }

    @Test
    public void isAlreadyAdded() {
        assertThat(sut.isAlreadyAdded(Arrays.asList("foo", "bar", "baz"), "foo"), is(true));
        assertThat(sut.isAlreadyAdded(Arrays.asList("foo", "bar", "baz"), "bar"), is(true));
        assertThat(sut.isAlreadyAdded(Arrays.asList("foo", "bar", "baz"), "baz"), is(true));

        assertThat(sut.isAlreadyAdded(Collections.emptyList(), null), is(false));
        assertThat(sut.isAlreadyAdded(Collections.emptyList(), "foo"), is(false));
        assertThat(sut.isAlreadyAdded(Arrays.asList("foo", "bar", "bar"), "snafu"), is(false));
        assertThat(sut.isAlreadyAdded(Arrays.asList("foo", "bar", "bar"), null), is(false));
    }
}
