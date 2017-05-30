package de.weltraumschaf.maconha.service.mediafile;

import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link KeywordsFromFileNameExtractor}.
 */
public class KeywordsFromFileNameExtractorTest {

    private final KeywordsFromFileNameExtractor sut = new KeywordsFromFileNameExtractor();

    @Test
    public void extract_fileOne() {
        assertThat(sut.extract("Filme/Musikvideos/Linkin_Park_-_What_I_ve_Done__Musikvideo_.avi"),
            containsInAnyOrder(
                "filme", "musikvideos", "linkin", "park", "what", "i", "ve", "done", "musikvideo"));
    }

    @Test
    public void extract_fileTwo() {
        assertThat(
            sut.extract("Filme/Kinofilme/Deutsch/Der_unglaubliche_Hulk.mp4"),
            containsInAnyOrder(
                "filme", "kinofilme", "deutsch", "der", "unglaubliche", "hulk"));
    }

    @Test
    public void extract_fileThree() {
        assertThat(
            sut.extract("Filme/Dokumentation/Alpha Centauri/Realmedia/Alpha Centauri 091 - Wird Licht muede - 020317.rm"),
            containsInAnyOrder(
                "filme", "dokumentation", "alpha", "centauri", "realmedia", "091", "wird", "licht", "muede", "020317"));
    }

    @Test
    public void extract_fileFour() {
        assertThat(
            sut.extract("Musik/Artists/Queensryche/Mindcrime At The Moore/24-Queensrÿche-Re-Arrange_You.mp3"),
            containsInAnyOrder(
                "musik", "artists", "queensryche", "mindcrime", "at", "the", "moore", "24", "queensrÿche", "re", "arrange", "you"));
    }

    @Test
    public void extract_fileFive() {
        assertThat(
            sut.extract("Musik/Artists/Heroes Del Silencio/1998 - rarezas/09 - acústica.mp3"),
            containsInAnyOrder(
                "musik", "artists", "heroes", "del", "silencio", "1998", "rarezas", "09", "acústica"));
    }

    @Test
    public void extract_fileSix() {
        assertThat(
            sut.extract("Musik/Artists/Control Machete/Control Machete - Si Señor.mp3"),
            containsInAnyOrder(
                "musik", "artists", "control", "machete", "si", "señor"));
    }

    @Test
    public void extract_fileSeven() {
        assertThat(
            sut.extract("Commedy/Tresenlesen/Tresenlesen - Das Auge liest mit/101 Auftakt durch Begrüßung.mp3"),
            containsInAnyOrder(
                "commedy", "tresenlesen", "das", "auge", "liest", "mit", "101", "auftakt", "durch", "begrüßung"));
    }

}
