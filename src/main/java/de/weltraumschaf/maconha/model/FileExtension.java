package de.weltraumschaf.maconha.model;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Super type for file extension enums.
 * <p>
 * File extension is the string portion after the last dot of a file name.
 * </p>
 */
public enum FileExtension {

    NONE(""),
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

    private static final Map<String, FileExtension> LOOKUP;

    static {
        final Map<String, FileExtension> tmp = new HashMap<>();

        for (final FileExtension ext : values()) {
            tmp.put(ext.getExtension().toLowerCase(), ext);
        }

        LOOKUP = Collections.unmodifiableMap(tmp);
    }

    private final String extension;

    FileExtension(final String extension) {
        this.extension = extension;
    }

    /**
     * Get the literal extension.
     *
     * @return never {@code null} nor empty
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Checks if the enum is the given extension.
     * <p>
     * This method ignores case of extension.
     * </p>
     *
     * @param fileExtension may be {@code null} or empty
     * @return {@code true} if it is the extension, else {@code false}
     */
    public boolean isExtension(final String fileExtension) {
        return extension.equalsIgnoreCase(fileExtension);
    }

    /**
     * Whether the given string is a known extension.
     *
     * @param fileExtension may be {@code null} or empty
     * @return {@code true} if known, else {@code false}
     */
    public static boolean hasValue(final String fileExtension) {
        return null != fileExtension && LOOKUP.containsKey(fileExtension.toLowerCase());

    }

    public static FileExtension forValue(final String fileExtension) {
        if (hasValue(fileExtension)) {
            return LOOKUP.get(fileExtension.toLowerCase());
        }

        throw new IllegalArgumentException(String.format("Unknown extension '%s'!", fileExtension));
    }

    /**
     * Extracts the extension from a given path.
     *
     * @see #extractExtension(java.lang.String)
     * @param input may be {@code null}
     * @return never {@code null}, as default {@link #NONE#getExtension()}
     */
    public static String extractExtension(final Path input) {
        if (null == input) {
            return NONE.getExtension();
        }

        return extractExtension(input.toString());
    }

    /**
     * Extracts the extension from a given path.
     * <p>
     * As extension the string portion after the last dot is recognized. the returned extension is without that point.
     * </p>
     *
     * @param input may be {@code null} or empty
     * @return never {@code null}, as default {@link #NONE#getExtension()}
     */
    public static String extractExtension(final String input) {
        if (null == input || input.trim().isEmpty()) {
            return NONE.getExtension();
        }

        return input.substring(input.lastIndexOf('.') + 1);
    }
}
