package de.weltraumschaf.maconha.service.scan.extraction;

import java.util.Collection;

/**
 * Implementations extracts keywords from given strings.
 *
 * @param <T> type of extracted data
 */
public interface KeywordExtractor<T> {

    /**
     * Extracts keywords from given input string.
     * <p>
     * The resulting collection does not contain duplicates.
     * </p>
     *
     * @param input maybe {@code null} or empty
     * @return never {@code null}, maybe empty
     */
    T extract(final String input) throws Exception;

}
