
package de.weltraumschaf.maconha.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link FileFinder}.
 */
public class FileFinderTest {

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    private final Set<? extends FileExtension> wantedFiles=
        EnumSet.of(Movies.APPLE_QUICKTIME_MOVIE, Movies.AUDIO_VIDEO_INTERLEAVE);
    private final FileFinder sut = new FileFinder(wantedFiles);

    @Test
    public void hasExtensionToCollect() {
        assertThat(sut.hasExtensionToCollect(null), is(false));
        assertThat(sut.hasExtensionToCollect(Paths.get("")), is(false));
        assertThat(sut.hasExtensionToCollect(Paths.get("/foo/bar.jpg")), is(false));
        assertThat(sut.hasExtensionToCollect(Paths.get("/bar.exe")), is(false));
        assertThat(sut.hasExtensionToCollect(Paths.get("/foo/bar/baz")), is(false));
        assertThat(sut.hasExtensionToCollect(Paths.get("snafu")), is(false));
        assertThat(sut.hasExtensionToCollect(Paths.get("snafu.mov")), is(true));
        assertThat(sut.hasExtensionToCollect(Paths.get("/foo/bar/baz/snafu.mov")), is(true));
        assertThat(sut.hasExtensionToCollect(Paths.get("/foo/snafu.avi")), is(true));
        assertThat(sut.hasExtensionToCollect(Paths.get("foo/snafu.avi")), is(true));
    }

    @Test
    public void find() throws IOException {
        tmp.newFolder("foo");
        final Path rootDir = tmp.getRoot().toPath();
        final Path one = rootDir.resolve("foo/bar.gif");
        Files.createFile(one);
        final Path two = rootDir.resolve("foo/bar.avi");
        Files.createFile(two);
        final Path three = rootDir.resolve("foo.mov");
        Files.createFile(three);
        final Path four = rootDir.resolve("foo.bar");
        Files.createFile(four);

        final Collection<Path> found = FileFinder.find(rootDir, wantedFiles);

        assertThat(found, containsInAnyOrder(two, three));
    }
}