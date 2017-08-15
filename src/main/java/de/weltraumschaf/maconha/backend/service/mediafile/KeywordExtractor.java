package de.weltraumschaf.maconha.backend.service.mediafile;

import java.util.Collection;

/**
 * Implementations extracts keywords from given strings.
 * <p>
 * The resulting collection does not contain duplicates.
 * </p>
 */
public interface KeywordExtractor extends Extractor<Collection<String>> {

}
