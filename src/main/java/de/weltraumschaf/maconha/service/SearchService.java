package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Media;
import java.util.Collection;

/**
 * Defines the interface for searching stuff.
 */
public interface SearchService {

    /**
     * Searches media files by given query.
     *
     * @param query may be {@code null} or empty
     * @return never {@code null}, unmodifiable
     */
    Collection<Media> search(final String query);

}
