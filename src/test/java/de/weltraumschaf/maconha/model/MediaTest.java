package de.weltraumschaf.maconha.model;

import de.weltraumschaf.maconha.core.FileExtension;
import de.weltraumschaf.maconha.model.Media.MediaType;
import nl.jqno.equalsverifier.EqualsVerifier;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link Media}.
 */
public class MediaTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier
            .forClass(Media.class)
            .withPrefabValues(OriginFile.class, new OriginFile().setId(1), new OriginFile().setId(2))
            .withPrefabValues(Keyword.class, new Keyword().setId(1), new Keyword().setId(2))
            .verify();
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
