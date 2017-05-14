package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.maconha.model.FileExtension;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link FileNameExtractor}.
 */
public class FileNameExtractorTest {

    private final String fileOne = "Filme/Musikvideos/Linkin_Park_-_What_I_ve_Done__Musikvideo_.avi";
    private final String fileTwo = "Filme/Kinofilme/Deutsch/Der_unglaubliche_Hulk.mp4";
    private final String fileThree = "Filme/Dokumentation/Alpha Centauri/Realmedia/Alpha Centauri 091 - Wird Licht muede - 020317.rm";
    private final String fileFour = "Musik/Artists/Queensryche/Mindcrime At The Moore/24-Queensrÿche-Re-Arrange_You.mp3";
    private final String fileFive = "Musik/Artists/Heroes Del Silencio/1998 - rarezas/09 - acústica.mp3";
    private final String fileSix = "Musik/Artists/Control Machete/Control Machete - Si Señor.mp3";
    private final String fileSeven = "Commedy/Tresenlesen/Tresenlesen - Das Auge liest mit/101 Auftakt durch Begrüßung.mp3";

    private final FileNameExtractor sut = new FileNameExtractor();

    @Test
    public void extractKeywords() {
        assertThat(sut.extractKeywords(fileOne),
            containsInAnyOrder(
                "filme", "musikvideos", "linkin", "park", "what", "i", "ve", "done", "musikvideo"));

        assertThat(
            sut.extractKeywords(fileTwo),
            containsInAnyOrder(
                "filme", "kinofilme", "deutsch", "der", "unglaubliche", "hulk"));

        assertThat(
            sut.extractKeywords(fileThree),
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
        assertThat(sut.splitCamelCase("Queensrÿche"), is("Queensrÿche"));
        assertThat(sut.splitCamelCase("Señor"), is("Señor"));
        assertThat(sut.splitCamelCase("Begrüßung"), is("Begrüßung"));
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
