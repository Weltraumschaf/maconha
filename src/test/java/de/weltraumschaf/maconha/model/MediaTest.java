package de.weltraumschaf.maconha.model;

import de.weltraumschaf.maconha.core.FileExtension;
import de.weltraumschaf.maconha.model.Media.MediaType;
import java.nio.file.Paths;
import nl.jqno.equalsverifier.EqualsVerifier;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import org.joda.time.LocalDateTime;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link Media}.
 */
public class MediaTest {

    final Media sut = new Media();

    @Test
    public void equalsAndHashCode() {
        final OriginFile fileOne = new OriginFile();
        fileOne.setId(1);
        final OriginFile fileTwo = new OriginFile();
        fileTwo.setId(2);
        EqualsVerifier
            .forClass(Media.class)
            .withPrefabValues(OriginFile.class, fileOne, fileTwo)
            .withPrefabValues(Keyword.class, new Keyword().setId(1), new Keyword().setId(2))
            .withIgnoredFields("keywords", "originFile")
            .verify();
    }

    @Test
    public void setOriginFile() {
        final OriginFile file = new OriginFile();

        sut.setOriginFile(file);

        assertThat(file.getImported(), is(sut));
    }

    @Test
    public void addKeyword() {
        final Keyword keywordOne = new Keyword();
        keywordOne.setLiteral("one");
        final Keyword keywordTwo = new Keyword();
        keywordTwo.setLiteral("two");

        sut.addKeyword(keywordOne);

        assertThat(keywordOne.getMedias(), contains(sut));
    }

    @Test
    public void toString_doesNotEndlessLoop() {
        sut.setFormat(FileExtension.MATROSKA_VIDEO_FILE);
        sut.setId(23);
        sut.setLastImported(new LocalDateTime(0));
        sut.setTitle("title");
        sut.setType(MediaType.VIDEO);

        assertThat(
            sut.toString(),
            is("Media{id=23, type=VIDEO, format=MATROSKA_VIDEO_FILE, title=title, lastImported=1970-01-01T01:00:00.000, originFile=, keywords=}"));

        final OriginFile file = new OriginFile();
        file.setAbsolutePath(Paths.get("/foo/bar"));
        sut.setOriginFile(file);
        assertThat(
            sut.toString(),
            is("Media{id=23, type=VIDEO, format=MATROSKA_VIDEO_FILE, title=title, lastImported=1970-01-01T01:00:00.000, originFile=/foo/bar, keywords=}"));

        final Keyword keywordOne = new Keyword();
        keywordOne.setLiteral("foo");
        sut.addKeyword(keywordOne);
        assertThat(
            sut.toString(),
            is("Media{id=23, type=VIDEO, format=MATROSKA_VIDEO_FILE, title=title, lastImported=1970-01-01T01:00:00.000, originFile=/foo/bar, keywords=foo}"));

        final Keyword keywordTwo = new Keyword();
        keywordTwo.setLiteral("bar");
        sut.addKeyword(keywordTwo);
        assertThat(
            sut.toString(),
            is("Media{id=23, type=VIDEO, format=MATROSKA_VIDEO_FILE, title=title, lastImported=1970-01-01T01:00:00.000, originFile=/foo/bar, keywords=bar, foo}"));

        final Keyword keywordThree = new Keyword();
        keywordThree.setLiteral("baz");
        sut.addKeyword(keywordThree);
        assertThat(
            sut.toString(),
            is("Media{id=23, type=VIDEO, format=MATROSKA_VIDEO_FILE, title=title, lastImported=1970-01-01T01:00:00.000, originFile=/foo/bar, keywords=bar, foo, baz}"));
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
        assertThat(MediaType.forValue(FileExtension.SHOCKWAVE_FLASH_MOVIE), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.WINDOWS_MEDIA_VIDEO_FILE), is(MediaType.VIDEO));
        assertThat(MediaType.forValue(FileExtension.XVID_ENCODED_VIDEO_FILE), is(MediaType.VIDEO));
    }

}
