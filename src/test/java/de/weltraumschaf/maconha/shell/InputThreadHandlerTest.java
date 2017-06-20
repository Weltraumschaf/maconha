package de.weltraumschaf.maconha.shell;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link InputThreadHandler}.
 */
public final class InputThreadHandlerTest {
    @Test
    public void run_empty() {
        final InputStream input = new ByteArrayInputStream(new byte[0]);
        final InputThreadHandler sut = new InputThreadHandler(input);

        sut.run();

        assertThat(sut.getOutput(), is(""));
    }

    @Test
    public void run_someLines() {
        final String text = "foo\nbar\nbaz\n";
        final InputStream input = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        final InputThreadHandler sut = new InputThreadHandler(input);

        sut.run();

        assertThat(sut.getOutput(), is("foo\nbar\nbaz"));
    }

    @Test
    public void run_someLinesWithWindowsLineENdings() {
        final String text = "foo\r\nbar\r\nbaz\r\n";
        final InputStream input = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        final InputThreadHandler sut = new InputThreadHandler(input);

        sut.run();

        assertThat(sut.getOutput(), is("foo\nbar\nbaz"));
    }
}
