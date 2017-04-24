package de.weltraumschaf.maconha.service.search;

import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.service.SearchService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * Default implementation.
 * <p>
 * This class is package private because it must not be used directly from outside. Use the DI of Spring to get an
 * instance.
 * </p>
 */
@Service
final class DefaultSearchService implements SearchService {

    @Override
    public Collection<MediaFile> forKeywords(final Collection<String> keywords) {
        return Collections.emptyList();
    }
}
