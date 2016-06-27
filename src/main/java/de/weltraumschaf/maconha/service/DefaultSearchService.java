package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Default implementation.
 */
@Service
final class DefaultSearchService implements SearchService {


    private final KeywordRepo keywords;

    @Autowired
    public DefaultSearchService(final KeywordRepo keywords) {
        super();
        this.keywords = keywords;
    }

    @Override
    public Collection<Media> search(final String query) {
        if (null == query|| query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        final Collection<Keyword> found = keywords.findByLiterals(splitQuery(query));
        return Collections.unmodifiableCollection(prepare(found));
    }

    Collection<String> splitQuery(final String query) {
        return Arrays.asList(query.trim().split("\\s+"));
    }

    private Collection<Media> prepare(final Collection<Keyword> found) {
        final Collection<Media> result = new ArrayList<>();


        return result;
    }
}
