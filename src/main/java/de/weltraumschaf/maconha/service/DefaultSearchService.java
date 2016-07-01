package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.controller.ApiController;
import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Default implementation.
 */
@Service
final class DefaultSearchService implements SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSearchService.class);
    private final KeywordRepo keywords;

    @Autowired
    public DefaultSearchService(final KeywordRepo keywords) {
        super();
        this.keywords = keywords;
    }

    @Override
    public Collection<Media> search(final String query) {
        if (null == query || query.trim().isEmpty()) {
            LOGGER.debug("Do not search because query was empty.");
            return Collections.emptyList();
        }

        final Collection<String> splitedQuery = splitQuery(query);
        LOGGER.debug("Search for keywords {}.", splitedQuery);
        final Collection<Keyword> found = keywords.findByLiteralIn(splitedQuery);
        LOGGER.debug("Found number keywords {}.", found.size());
        return Collections.unmodifiableCollection(prepare(found));
    }

    Collection<String> splitQuery(final String query) {
        return Arrays.asList(query.trim().split("\\s+"));
    }

    private Collection<Media> prepare(final Collection<Keyword> keywords) {
        final Set<Media> unique = keywords.stream()
            .map(keyword -> keyword.getMedias())
            .flatMap(medias -> medias.stream())
            .collect(Collectors.toSet());
        final List<Media> sorted = new ArrayList<>(unique);
        Collections.sort(sorted, new SotByMatchedKeywords(keywords));

        return sorted;
    }

    static final class SotByMatchedKeywords implements Comparator<Media> {

        private final Collection<Keyword> keywords;

        SotByMatchedKeywords(final Collection<Keyword> keywords) {
            super();
            this.keywords = new ArrayList<>(keywords);
        }

        @Override
        public int compare(final Media left, final Media right) {
            final int leftCount = countContainingKeywords(left);
            final int rightCount = countContainingKeywords(right);

            if (leftCount < rightCount) {
                return 1;
            } else if (leftCount > rightCount) {
                return -1;
            }

            return 0;
        }

        int countContainingKeywords(final Media media) {
            if (null == media) {
                return 0;
            }

            return keywords.stream()
                .filter(keyword -> media.getKeywords().contains(keyword))
                .map(item -> 1)
                .reduce(0, Integer::sum);
        }
    }
}
