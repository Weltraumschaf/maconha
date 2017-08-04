package de.weltraumschaf.maconha.backend.service.keywords;

import de.weltraumschaf.maconha.backend.model.entity.Keyword;
import de.weltraumschaf.maconha.backend.repo.KeywordRepo;
import de.weltraumschaf.maconha.backend.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Default implementation.
 */
@Service
class DefaultKeywordService implements KeywordService {
    private final KeywordRepo keywords;

    @Autowired
    DefaultKeywordService(final KeywordRepo keywords) {
        super();
        this.keywords = keywords;
    }

    @Override
    public Collection<Keyword> topTen() {
        final Collection<Keyword> topTen = new ArrayList<>();

        for (int i = 1; i <= 10; ++i) {
            final Keyword keyword = new Keyword();
            keyword.setLiteral("keyword" + i);
            topTen.add(keyword);
        }

        return Collections.unmodifiableCollection(topTen);
    }

    @Override
    public long numberOfKeywords() {
        return keywords.count();
    }
}
