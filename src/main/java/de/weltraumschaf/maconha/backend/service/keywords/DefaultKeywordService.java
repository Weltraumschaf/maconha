package de.weltraumschaf.maconha.backend.service.keywords;

import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.KeywordAndNumberOfMediaFiles;
import de.weltraumschaf.maconha.backend.model.entity.Keyword;
import de.weltraumschaf.maconha.backend.repo.KeywordRepo;
import de.weltraumschaf.maconha.backend.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation.
 */
@Service
class DefaultKeywordService implements KeywordService, HasLogger {
    private final KeywordRepo keywords;

    @Autowired
    DefaultKeywordService(final KeywordRepo keywords) {
        super();
        this.keywords = keywords;
    }

    @Override
    public Collection<KeywordAndNumberOfMediaFiles> topTen() {
        logger().debug("Find top ten keywords. {}");

        final List<KeywordAndNumberOfMediaFiles> topTen = keywords.topTen()
            .stream()
            .map(o -> {
                final String literal = (String) o[0];
                final BigInteger numberOfMediaFiles = (BigInteger) o[1];
                return new KeywordAndNumberOfMediaFiles(literal, numberOfMediaFiles.longValue());
            }).collect(Collectors.toList());

        return Collections.unmodifiableCollection(topTen);
    }

    @Override
    public long numberOfKeywords() {
        return keywords.count();
    }
}
