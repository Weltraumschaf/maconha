package de.weltraumschaf.maconha.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import org.joda.time.LocalDateTime;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link MediaFile}.
 */
public class MediaFileTest {

    private final MediaFile sut = new MediaFile();

    @Test
    public void equalsAndHashCode() {
        final Keyword keywordOne = new Keyword();
        keywordOne.setLiteral("foo");
        final Keyword keywordTwo = new Keyword();
        keywordTwo.setLiteral("bar");
        final Bucket bucketOne = new Bucket();
        bucketOne.setDirectory("foo");
        final Bucket bucketTwo = new Bucket();
        bucketTwo.setDirectory("bar");

        EqualsVerifier
            .forClass(MediaFile.class)
            .withPrefabValues(Keyword.class, keywordOne, keywordTwo)
            .withPrefabValues(Bucket.class, bucketOne, bucketTwo)
            .withIgnoredFields("id", "keywords")
            .verify();
    }

    @Test
    public void addKeyword() {
        final Keyword keywordOne = new Keyword();
        keywordOne.setLiteral("one");
        final Keyword keywordTwo = new Keyword();
        keywordTwo.setLiteral("two");

        sut.addKeyword(keywordOne);

        assertThat(keywordOne.getMediaFiles(), contains(sut));
    }

    @Test
    public void toString_doesNotEndlessLoop() {
        sut.setFormat(FileExtension.MATROSKA_VIDEO_FILE);
        sut.setId(23);
        sut.setLastScanned(new LocalDateTime(0));
        sut.setRelativeFileName("title");
        sut.setType(MediaType.VIDEO);

        assertThat(
            sut.toString(),
            is("MediaFile{id=23, type=VIDEO, format=MATROSKA_VIDEO_FILE, relativeFileName=title, fileHash=, lastScanned=1970-01-01T01:00:00.000, keywords=, bucket=null}"));

        assertThat(
            sut.toString(),
            is("MediaFile{id=23, type=VIDEO, format=MATROSKA_VIDEO_FILE, relativeFileName=title, fileHash=, lastScanned=1970-01-01T01:00:00.000, keywords=, bucket=null}"));

        final Keyword keywordOne = new Keyword();
        keywordOne.setLiteral("foo");
        sut.addKeyword(keywordOne);
        assertThat(
            sut.toString(),
            is("MediaFile{id=23, type=VIDEO, format=MATROSKA_VIDEO_FILE, relativeFileName=title, fileHash=, lastScanned=1970-01-01T01:00:00.000, keywords=foo, bucket=null}"));

        final Keyword keywordTwo = new Keyword();
        keywordTwo.setLiteral("bar");
        sut.addKeyword(keywordTwo);
        assertThat(
            sut.toString(),
            is("MediaFile{id=23, type=VIDEO, format=MATROSKA_VIDEO_FILE, relativeFileName=title, fileHash=, lastScanned=1970-01-01T01:00:00.000, keywords=bar, foo, bucket=null}"));

        final Keyword keywordThree = new Keyword();
        keywordThree.setLiteral("baz");
        sut.addKeyword(keywordThree);
        assertThat(
            sut.toString(),
            is("MediaFile{id=23, type=VIDEO, format=MATROSKA_VIDEO_FILE, relativeFileName=title, fileHash=, lastScanned=1970-01-01T01:00:00.000, keywords=bar, foo, baz, bucket=null}"));
    }

    @Test
    public void MediaType_forValue() {
        assertThat(MediaType.forValue(null), is(MediaType.OTHER));
        assertThat(MediaType.forValue(FileExtension.NONE), is(MediaType.OTHER));
        assertThat(MediaType.forValue(FileExtension.AUDIO_VIDEO_INTERLEAVE), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.DIVX_ENCODED_MOVIE_FILE), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.ITUNES_VIDEO_FILE), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.MATROSKA_VIDEO_FILE), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.APPLE_QUICKTIME_MOVIE), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.MPEG4_VIDEO_FILE), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.MPEG_MOVIE), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.MPEG_VIDEO_FILE), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.OGG_MEDIA_FILE), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.REAL_MEDIA_FILE), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.SHOCKWAVE_FLASH), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.WINDOWS_MEDIA_VIDEO_FILE), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.XVID_ENCODED_VIDEO_FILE), is(MediaType.VIDEO));
    }

}
