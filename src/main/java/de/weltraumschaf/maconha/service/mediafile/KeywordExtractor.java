package de.weltraumschaf.maconha.service.mediafile;

import java.util.Collection;

/**
 * Implementations extracts keywords from given strings.
 * <p>
 * The resulting collection does not contain duplicates.
 * </p>
 */
interface KeywordExtractor extends Extractor<Collection<String>> {

}
