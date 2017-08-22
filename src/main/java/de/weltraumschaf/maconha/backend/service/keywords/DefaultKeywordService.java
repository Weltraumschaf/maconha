package de.weltraumschaf.maconha.backend.service.keywords;

import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.KeywordAndNumberOfMediaFiles;
import de.weltraumschaf.maconha.backend.model.entity.Keyword;
import de.weltraumschaf.maconha.backend.repo.KeywordRepo;
import de.weltraumschaf.maconha.backend.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        logger().debug("Find top ten keywords.");
        final List<Keyword> allKeywords = keywords.findAll();
        allKeywords.sort((keyword1, keyword2) -> {
            final Integer numberOfMediaFiles1 = keyword1.getMediaFiles().size();
            final Integer numberOfMediaFiles2 = keyword2.getMediaFiles().size();
            return numberOfMediaFiles2.compareTo(numberOfMediaFiles1);
        });

        final List<KeywordAndNumberOfMediaFiles> mapped = allKeywords.subList(0, 10)
            .stream()
            .map(k -> new KeywordAndNumberOfMediaFiles(k.getLiteral(), k.getMediaFiles().size()))
            .collect(Collectors.toList());
        return Collections.unmodifiableCollection(mapped);
    }

    @Override
    public long numberOfKeywords() {
        return keywords.count();
    }
}
