package de.weltraumschaf.maconha.service.scan.extraction;

import java.util.Collection;

/**
 * Implementations extracts keywords from given strings.
 */
public interface KeywordExtractor {

    /**
     * Extracts keywords from given input string.
     * <p>
     * The resulting collection does not contain duplicates.
     * </p>
     *
     * @param input maybe {@code null} or empty
     * @return never {@code null}, maybe empty
     */
    Collection<String> extract(final String input);

}
