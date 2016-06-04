package de.weltraumschaf.maconha.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link FileNameExtractor}.
 */
public class FileNameExtractorTest {

    private final Path fileOne = Paths.get(
        "/Volumes/Blackhole/Filme/Musikvideos/Linkin_Park_-_What_I_ve_Done__Musikvideo_.avi");
    private final Path fileTwo = Paths.get(
        "/Volumes/Blackhole/Filme/Kinofilme/Deutsch/Der_unglaubliche_Hulk.mp4");
    private final Path fileThree = Paths.get(
        "/Volumes/Blackhole/Filme/Dokumentation/Alpha Centauri/Realmedia/Alpha Centauri 091 - Wird Licht müde - 020317.rm");
    private final FileNameExtractor sut = new FileNameExtractor();

    @Test
    public void extractKeywords() {
        assertThat(sut.extractKeywords(fileOne),
            containsInAnyOrder(
                "volumes", "blackhole", "filme", "musikvideos", "linkin", "park", "what", "i", "ve", "done",
                "musikvideo", "avi"));

        assertThat(
            sut.extractKeywords(fileTwo),
            containsInAnyOrder(
                "volumes", "blackhole", "filme", "kinofilme", "deutsch", "der", "unglaubliche", "hulk", "mp4"));

        assertThat(
            sut.extractKeywords(fileThree),
            containsInAnyOrder(
                "volumes", "blackhole", "filme", "dokumentation", "alpha", "centauri", "realmedia", "091", "wird",
                "licht", "müde", "020317", "rm"));
    }

    @Test
    public void extractTitle() {
        assertThat(sut.extractTitle(fileOne), is("Linkin Park - What I ve Done Musikvideo"));
        assertThat(sut.extractTitle(fileTwo), is("Der unglaubliche Hulk"));
        assertThat(sut.extractTitle(fileThree), is("Alpha Centauri 091 - Wird Licht müde - 020317"));
    }

    @Test
    public void extractExtension() {
        assertThat(sut.extractExtension(fileOne), is(Movies.AUDIO_VIDEO_INTERLEAVE));
        assertThat(sut.extractExtension(fileTwo), is(Movies.MPEG4_VIDEO_FILE));
        assertThat(sut.extractExtension(fileThree), is(Movies.REAL_MEDIA_FILE));
    }

}