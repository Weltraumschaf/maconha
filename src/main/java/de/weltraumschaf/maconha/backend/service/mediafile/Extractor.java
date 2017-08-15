package de.weltraumschaf.maconha.backend.service.mediafile;

/**
 * Extracts data from a given string.
 */
public interface Extractor<T> {
    /**
     * Extracts metadata from given string in ny way.
     *
     * @param input maybe {@code null} or blank
     * @return never {@code null} returns a reasonable default if nothing can't be extracted or throws an exception
     */
    T extract(final String input);
}
