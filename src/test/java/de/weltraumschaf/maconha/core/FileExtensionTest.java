package de.weltraumschaf.maconha.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link FileExtension}.
 */
public class FileExtensionTest {

    @Test
    public void hasValue_false() {
        assertThat(FileExtension.hasValue(null), is(false));
        assertThat(FileExtension.hasValue(""), is(false));
    }

    @Test
    public void hasValue_trueForMovues() {
        for (final Movies m : Movies.values()){
            assertThat(FileExtension.hasValue(m.getExtension()), is(true));
        }
    }

    @Test
    public void extractExtension_path() {
        assertThat(FileExtension.extractExtension((Path) null), is(""));
        assertThat(FileExtension.extractExtension(Paths.get("")), is(""));
        assertThat(FileExtension.extractExtension(Paths.get("snafu.foo")), is("foo"));
        assertThat(FileExtension.extractExtension(Paths.get("foo.bar.baz")), is("baz"));
        assertThat(FileExtension.extractExtension(Paths.get("/bla.blub/snafu/foo.bar.baz")), is("baz"));
    }

    @Test
    public void extractExtension_string() {
        assertThat(FileExtension.extractExtension((String) null), is(""));
        assertThat(FileExtension.extractExtension(""), is(""));
        assertThat(FileExtension.extractExtension("snafu.foo"), is("foo"));
        assertThat(FileExtension.extractExtension("foo.bar.baz"), is("baz"));
        assertThat(FileExtension.extractExtension("/bla.blub/snafu/foo.bar.baz"), is("baz"));
    }
}
