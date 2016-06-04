package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Media;
import java.util.Collection;

/**
 * Defines the interface for searching stuff.
 */
public interface SearchService {

    Collection<Media> search(final String query);

}
