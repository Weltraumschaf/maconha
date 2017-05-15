package de.weltraumschaf.maconha.service.scan.extraction;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link KeywordsFromMetaDataExtractor}.
 */
public final class KeywordsFromMetaDataExtractorTest {

    private static final String ONE =
        "The Sound of Musik\n" +
            "Falco\n" +
            "Falco\n" +
            "Ferdi Bolland & Rob Bolland\n" +
            "Live Forever\n" +
            "1999-11-29T08:00:00Z\n" +
            "2017-03-15 20:39:04\n" +
            "Live Forever\n";
    private static final String TWO =
        "Hey You\n" +
            "Pink Floyd\n" +
            "Pink Floyd\n" +
            "Roger Waters\n" +
            "A Foot In the Door: The Best of Pink Floyd\n" +
            "1979-11-30T08:00:00Z\n" +
            "2017-01-09 09:51:58\n" +
            "Foot In the Door: The Best of Pink Floyd\n" +
            "Warner:isrc:GBN9Y1100108\n";
    private static final String THREE =
        "True Faith '94\n" +
            "New Order\n" +
            "New Order\n" +
            "Bernard Sumner, Gilbert, Hook, Morris & Stephen Hague\n" +
            "The Best of New Order\n" +
            "1994-11-22T08:00:00Z\n" +
            "2016-11-30 09:22:40\n" +
            "Best of New Order\n";

    private final KeywordsFromMetaDataExtractor sut = new KeywordsFromMetaDataExtractor();

    @Test
    public void extract_one() throws Exception {
        assertThat(
            sut.extract(ONE),
            containsInAnyOrder("11", "00", "03", "rob", "15", "04", "39", "sound", "29", "08", "the", "2017",
                "ferdi", "t", "falco", "of", "z", "musik", "bolland", "forever", "live", "20", "1999"));
    }

    @Test
    public void extract_two() throws Exception {
        assertThat(
            sut.extract(TWO),
            containsInAnyOrder("00", "door", "01", "pink", "08", "09", "best", "roger", "of", "51", "hey",
                "1979", "30", "you", "foot", "11", "waters", "a", "58", "floyd", "in", "isrc", "warner", "the",
                "1100108", "gbn", "2017", "t", "9", "y", "z"));
    }

    @Test
    public void extract_three() throws Exception {
        assertThat(
            sut.extract(THREE),
            containsInAnyOrder("22", "00", "94", "bernard", "08", "faith", "09", "best", "sumner", "hook",
                "1994", "of", "morris", "30", "order", "11", "new", "gilbert", "stephen", "the", "t", "2016", "true",
                "hague", "z", "40"));
    }
}
