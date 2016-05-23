package de.weltraumschaf.maconha.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * enumerates common movie file extensions.
 *
 * http://fileinfo.com/extension/mov
 */
enum Movies implements FileExtension {

    AUDIO_VIDEO_INTERLEAVE("avi"),
    DIVX_ENCODED_MOVIE_FILE("divx"),
    ITUNES_VIDEO_FILE("m4v"),
    MATROSKA_VIDEO_FILE("mkv"),
    APPLE_QUICKTIME_MOVIE("mov"),
    MPEG4_VIDEO_FILE("mp4"),
    MPEG_MOVIE("mpeg"),
    MPEG_VIDEO_FILE("mpg"),
    OGG_MEDIA_FILE("ogm"),
    REAL_MEDIA_FILE("rm"),
    SHOCKWAVE_FLASH_MOVIE("swf"),
    WINDOWS_MEDIA_VIDEO_FILE("wmv"),
    XVID_ENCODED_VIDEO_FILE("xvid");

    private static final Map<String, Movies> LOOKUP;
    static {
        final Map<String, Movies> tmp = new HashMap<>();

        for (final Movies ext : values() ) {
            tmp.put(ext.getExtension().toLowerCase(), ext);
        }

        LOOKUP = Collections.unmodifiableMap(tmp);
    }

    private final String extension;

    private Movies(final String extension) {
        this.extension = extension;
    }

    @Override
    public String getExtension() {
        return extension;
    }


    @Override
    public boolean isExtension(final String fileExtension) {
        return extension.equalsIgnoreCase(fileExtension);
    }

    public static boolean hasValue(final String fileExtension) {
        if (null == fileExtension) {
            return false;
        }

        return LOOKUP.containsKey(fileExtension.toLowerCase());
    }

    public static Movies forValue(final String fileExtension) {
        if (hasValue(fileExtension)) {
            return LOOKUP.get(fileExtension.toLowerCase());
        }

        throw new IllegalArgumentException(String.format("Unknown extension '%s'!", fileExtension));
    }

}
