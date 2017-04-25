package de.weltraumschaf.maconha.service.search;

import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.service.SearchService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSearchService.class);

    private final KeywordRepo keywords;

    /**
     * Here we use lazy autowiring to prevent dependency cycle.
     *
     * @param keywords must not be {@code null}
     */
    @Lazy
    @Autowired
    DefaultSearchService(final KeywordRepo keywords) {
        super();
        this.keywords = keywords;
    }

    @Override
    public Collection<MediaFile> forKeywords(final Collection<String> query) {
        Validate.notNull(query, "query");

        if (query.isEmpty()) {
            return Collections.emptyList();
        }

        LOGGER.debug("Search for {}.", query);
        final Collection<Keyword> found = keywords.findByLiteralIn(query);
        LOGGER.debug("Found: {}", found);
        return Collections.emptyList();
    }
}
