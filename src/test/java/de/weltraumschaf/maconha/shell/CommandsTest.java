
package de.weltraumschaf.maconha.shell;

import java.nio.file.Paths;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link Commands}.
 */
public class CommandsTest {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_pathMustNotBeNull() {
        new Commands(null);
    }

    @Test(expected = NullPointerException.class)
    public void dirhash_directoryMustNotBeNull(){
        new Commands(tmp.getRoot().toPath()).dirhash(null);
    }

    @Test
    public void dirhash() {
        final Command dirhash = new Commands(tmp.getRoot().toPath()).dirhash(Paths.get("/foo/bar"));

        assertThat(dirhash, is(not(nullValue())));
        assertThat(dirhash.getPath(), is(tmp.getRoot().toPath()));
        assertThat(dirhash.getArguments(), is("/foo/bar"));
    }
}