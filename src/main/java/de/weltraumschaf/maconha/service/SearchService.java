package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.MediaFile;

import java.util.Collection;

/**
 * This service provides the business logic to find {@link MediaFile media files}.
 */
public interface SearchService {
    Collection<MediaFile> forKeywords(Collection<String> keywords);
}
