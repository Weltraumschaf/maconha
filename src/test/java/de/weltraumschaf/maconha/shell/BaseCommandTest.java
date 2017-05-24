package de.weltraumschaf.maconha.shell;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link de.weltraumschaf.maconha.shell.BaseCommand}.
 */
public class BaseCommandTest {

    private final de.weltraumschaf.maconha.shell.BaseCommand sut = new de.weltraumschaf.maconha.shell.BaseCommand(Paths.get("foo"), "bar", "-baz snafu") {
        };
    @Test(expected = NullPointerException.class)
    public void constructor_pathMustNotBeNull() {
        new de.weltraumschaf.maconha.shell.BaseCommand(null, "foo", "") {
        };
    }


    @Test(expected = NullPointerException.class)
    public void constructor_commandMustNotBeNull() {
        new de.weltraumschaf.maconha.shell.BaseCommand(Paths.get("foo"), null, "") {
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_commandMustNotBeEmpty() {
        new de.weltraumschaf.maconha.shell.BaseCommand(Paths.get("foo"), "", "") {
        };
    }

    @Test(expected = NullPointerException.class)
    public void constructor_argumentsMustNotBeNull() {
        new de.weltraumschaf.maconha.shell.BaseCommand(Paths.get("foo"), "bar", null) {
        };
    }

    @Test
    public void execute() throws IOException, InterruptedException {
        final Process process = mock(Process.class);
        when(process.getInputStream()).thenReturn(new ByteArrayInputStream("std out ...".getBytes()));
        when(process.getErrorStream()).thenReturn(new ByteArrayInputStream("std err\n snafu ...".getBytes()));
        when(process.waitFor()).thenReturn(42);
        final de.weltraumschaf.maconha.shell.ProcessBuilderWrapper builder = mock(de.weltraumschaf.maconha.shell.ProcessBuilderWrapper.class);
        when(builder.start("foo/bar", "-baz snafu")).thenReturn(process);
        sut.setBuilder(builder);

        assertThat(sut.execute(), is(new Result(42, "std out ...", "std err\n snafu ...")));
    }
}
