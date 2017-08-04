package de.weltraumschaf.maconha.backend.service;

import de.weltraumschaf.maconha.backend.model.entity.Keyword;

import java.util.Collection;

/**
 * Service to deal with {@link Keyword keywords}.
 */
public interface KeywordService {

    /**
     * Get the top ten keywords.
     * <p>
     * Top ten means the top ten found most in media files.
     * </p>
     *
     * @return never {@code null}, unmodifiable
     */
    Collection<Keyword> topTen();

    long numberOfKeywords();
}
