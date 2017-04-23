package de.weltraumschaf.maconha.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import de.weltraumschaf.maconha.model.FileExtension;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link FileNameExtractor}.
 */
public class FileNameExtractorTest {

    private final Path baseDir = Paths.get("/Volumes/Blackhole");
    private final Path fileOne = baseDir.resolve(
        "Filme/Musikvideos/Linkin_Park_-_What_I_ve_Done__Musikvideo_.avi");
    private final Path fileTwo = baseDir.resolve(
        "Filme/Kinofilme/Deutsch/Der_unglaubliche_Hulk.mp4");
    private final Path fileThree = baseDir.resolve(
        "Filme/Dokumentation/Alpha Centauri/Realmedia/Alpha Centauri 091 - Wird Licht muede - 020317.rm");
    private final FileNameExtractor sut = new FileNameExtractor();

    @Test
    public void relativeToBaseDir() {
        assertThat(
            sut.relativeToBaseDir(baseDir, fileOne),
            is("Filme/Musikvideos/Linkin_Park_-_What_I_ve_Done__Musikvideo_.avi"));
        assertThat(
            sut.relativeToBaseDir(baseDir, fileTwo),
            is("Filme/Kinofilme/Deutsch/Der_unglaubliche_Hulk.mp4"));
        assertThat(
            sut.relativeToBaseDir(baseDir, fileThree),
            is("Filme/Dokumentation/Alpha Centauri/Realmedia/Alpha Centauri 091 - Wird Licht muede - 020317.rm"));
    }

    @Test
    public void extractKeywords() {
        assertThat(sut.extractKeywords(baseDir, fileOne),
            containsInAnyOrder(
                "filme", "musikvideos", "linkin", "park", "what", "i", "ve", "done", "musikvideo"));

        assertThat(
            sut.extractKeywords(baseDir, fileTwo),
            containsInAnyOrder(
                "filme", "kinofilme", "deutsch", "der", "unglaubliche", "hulk"));

        assertThat(
            sut.extractKeywords(baseDir, fileThree),
            containsInAnyOrder(
                "filme", "dokumentation", "alpha", "centauri", "realmedia", "091", "wird", "licht", "muede", "020317"));
    }

    @Test
    public void extractTitle() {
        assertThat(sut.extractTitle(fileOne), is("Linkin Park - What I ve Done Musikvideo"));
        assertThat(sut.extractTitle(fileTwo), is("Der unglaubliche Hulk"));
        assertThat(sut.extractTitle(fileThree), is("Alpha Centauri 091 - Wird Licht muede - 020317"));
    }

    @Test
    public void extractExtension() {
        assertThat(sut.extractExtension(fileOne), is(FileExtension.AUDIO_VIDEO_INTERLEAVE));
        assertThat(sut.extractExtension(fileTwo), is(FileExtension.MPEG4_VIDEO_FILE));
        assertThat(sut.extractExtension(fileThree), is(FileExtension.REAL_MEDIA_FILE));
    }

    @Test
    public void splitCamelCase() {
        assertThat(sut.splitCamelCase(null), is(""));
        assertThat(sut.splitCamelCase(""), is(""));
        assertThat(sut.splitCamelCase("   "), is(""));
        assertThat(sut.splitCamelCase("FooBarBaz"), is("Foo Bar Baz"));
        assertThat(sut.splitCamelCase("foo"), is("foo"));
        assertThat(sut.splitCamelCase("FOO"), is("FOO"));
        assertThat(sut.splitCamelCase("Foo"), is("Foo"));
        assertThat(sut.splitCamelCase("FOOBar"), is("FOO Bar"));
        assertThat(sut.splitCamelCase("Foo42"), is("Foo 42"));
    }

    @Test
    public void replaceAndSplitDashes() {
        assertThat(sut.replaceAndSplitDashes(null), is(""));
        assertThat(sut.replaceAndSplitDashes(""), is(""));
        assertThat(sut.replaceAndSplitDashes("   "), is(""));
        assertThat(sut.replaceAndSplitDashes("foo-bar"), is("foo bar"));
        assertThat(sut.replaceAndSplitDashes("foo - bar"), is("foo - bar"));
    }
}
