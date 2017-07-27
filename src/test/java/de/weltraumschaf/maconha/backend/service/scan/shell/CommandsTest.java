
package de.weltraumschaf.maconha.backend.service.scan.shell;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Paths;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

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
    @SuppressWarnings("unchecked")
    public void dirhash() {
        final Command dirhash = new Commands(tmp.getRoot().toPath()).dirhash(Paths.get("/foo/bar"));

        assertThat(dirhash, is(not(nullValue())));
        final Class expectedType = Dirhash.class;
        assertThat(dirhash, isA(expectedType));
    }
}
