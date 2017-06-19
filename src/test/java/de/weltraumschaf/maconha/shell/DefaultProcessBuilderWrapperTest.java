package de.weltraumschaf.maconha.shell;

import org.junit.Test;

import java.io.IOException;

/**
 * Tests for {@link DefaultProcessBuilderWrapper}.
 */
public final class DefaultProcessBuilderWrapperTest {
    private final DefaultProcessBuilderWrapper sut = new DefaultProcessBuilderWrapper();

    @Test(expected = NullPointerException.class)
    public void start_commandArgumentMustNotBeNull() throws IOException {
        sut.start((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void start_commandArgumentsMustNotContainNull() throws IOException {
        sut.start("foo", null, "bar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void start_commandArgumentMustNotBeEmpty() throws IOException {
        sut.start();
    }
}
