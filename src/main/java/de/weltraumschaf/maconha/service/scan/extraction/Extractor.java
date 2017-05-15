package de.weltraumschaf.maconha.service.scan.extraction;

/**
 * Extracts data from a given string.
 */
public interface Extractor<T> {
    /**
     * Extracts metadata from given string in ny way.
     *
     * @param input maybe {@code null} or blank
     * @return never {@code null} returns a reasonable default if nothing can't be extracted or throws an exception
     * @throws Exception if the extraction failed in any way
     */
    T extract(final String input) throws Exception;
}