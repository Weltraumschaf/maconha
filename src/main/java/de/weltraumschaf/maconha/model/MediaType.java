package de.weltraumschaf.maconha.model;

import java.util.*;

/**
 * Type of media file.
 */
public enum MediaType {

    /**
     * All video media types.
     */
    VIDEO,
    /**
     * All audio media types.
     */
    AUDIO,
    /**
     * Everything not covered by any other enum.
     */
    OTHER;

    /**
     * All known file extensions for video file formats.
     */
    private static final Collection<FileExtension> VIDEOS = Collections.unmodifiableCollection(Arrays.asList(
        FileExtension.AUDIO_VIDEO_INTERLEAVE,
        FileExtension.DIVX_ENCODED_MOVIE_FILE,
        FileExtension.ITUNES_VIDEO_FILE,
        FileExtension.MATROSKA_VIDEO_FILE,
        FileExtension.APPLE_QUICKTIME_MOVIE,
        FileExtension.MPEG4_VIDEO_FILE,
        FileExtension.MPEG_MOVIE,
        FileExtension.MPEG_VIDEO_FILE,
        FileExtension.OGG_MEDIA_FILE,
        FileExtension.REAL_MEDIA_FILE,
        FileExtension.SHOCKWAVE_FLASH_MOVIE,
        FileExtension.WINDOWS_MEDIA_VIDEO_FILE,
        FileExtension.XVID_ENCODED_VIDEO_FILE
    ));
    /**
     * All known file extensions for audio file formats.
     */
    private static final Collection<FileExtension> AUDIOS = Collections.unmodifiableCollection(Collections.emptyList());

    private static final Map<MediaType, Collection<FileExtension>> LOOKUP;

    static {
        final Map<MediaType, Collection<FileExtension>> tmp = new HashMap<>();
        tmp.put(VIDEO, VIDEOS);
        tmp.put(AUDIO, AUDIOS);
        LOOKUP = Collections.unmodifiableMap(tmp);
    }

    public static MediaType forValue(final FileExtension extension) {
        if (null == extension) {
            return OTHER;
        }

        for (final Map.Entry<MediaType, Collection<FileExtension>> pair : LOOKUP.entrySet()) {
            if (pair.getValue().contains(extension)) {
                return pair.getKey();
            }
        }

        return OTHER;
    }
}