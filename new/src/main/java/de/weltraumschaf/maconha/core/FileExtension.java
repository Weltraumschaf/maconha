package de.weltraumschaf.maconha.core;

import java.nio.file.Path;

/**
 * Super type for file extension enums.
 * <p>
 * File extension is the string portion after the last dot of a file name.
 * </p>
 */
interface FileExtension {

    /**
     * The default if none found.
     */
    FileExtension NONE = new FileExtension() {

        @Override
        public String getExtension() {
            return "";
        }

        @Override
        public boolean isExtension(final String fileExtension) {
            return false;
        }
    };

    /**
     * Get the literal extension.
     *
     * @return never {@code null} nor empty
     */
    String getExtension();

    /**
     * Checks if the enum is the given extension.
     * <p>
     * This method ignores case of extension.
     * </p>
     *
     * @param fileExtension may be {@code null} or empty
     * @return {@code true} if it is the extension, else {@code false}
     */
    boolean isExtension(String fileExtension);

    /**
     * Whether the given string is a known extension.
     *
     * @param fileExtension may be {@code null} or empty
     * @return {@code true} if known, else {@code false}
     */
    static boolean hasValue(final String fileExtension) {
        return Movies.hasValue(fileExtension);
    }

    /**
     * Extracts the extension from a given path.
     *
     * @see #extractExtension(java.lang.String)
     * @param input may be {@code null}
     * @return never {@code null}, as default {@link #NONE#getExtension()}
     */
    static String extractExtension(final Path input) {
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
    static String extractExtension(final String input) {
        if (null == input || input.trim().isEmpty()) {
            return NONE.getExtension();
        }

        return input.substring(input.lastIndexOf('.') + 1);
    }
}
