package de.weltraumschaf.maconha.backend.service.search;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.predicate.NotFound;
import de.weltraumschaf.maconha.backend.model.entity.Keyword;
import de.weltraumschaf.maconha.backend.model.entity.MediaFile;
import de.weltraumschaf.maconha.backend.model.MediaType;
import de.weltraumschaf.maconha.backend.repo.KeywordRepo;
import de.weltraumschaf.maconha.backend.repo.MediaFileRepo;
import de.weltraumschaf.maconha.backend.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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
    private final MediaFileRepo mediaFiles;

    /**
     * Here we use lazy autowiring to prevent dependency cycle.
     *
     * @param keywords must not be {@code null}
     * @param mediaFiles must not be {@code null}
     */
    @Lazy
    @Autowired
    DefaultSearchService(final KeywordRepo keywords, final MediaFileRepo mediaFiles) {
        super();
        this.keywords = keywords;
        this.mediaFiles = mediaFiles;
    }

    @Override
    public Collection<MediaFile> forKeywords(final Collection<String> query, final Collection<MediaType> types) {
        Validate.notNull(query, "query");
        Validate.notNull(types, "types");

        if (query.isEmpty()) {
            return Collections.emptyList();
        }

        LOGGER.debug("Search for: {}.", query);
        final Collection<Keyword> foundKeywords = keywords.findByLiteralIn(query);
        LOGGER.debug("found {} keywords.", foundKeywords.size());
        final Collection<MediaFile> foundFiles = reduce(foundKeywords, types);

        return rank(foundFiles, foundKeywords);
    }

    private Collection<MediaFile> reduce(final Collection<Keyword> foundKeywords, final Collection<MediaType> types) {
        final Collection<Long> alreadyAdded = new ArrayList<>();
        final Collection<MediaFile> foundFiles = new ArrayList<>();

        foundKeywords.stream()
            .flatMap(k -> k.getMediaFiles().stream())
            .forEach(file -> {
                if (alreadyAdded.contains(file.getId())) {
                    return;
                }

                if (types.contains(file.getType())) {
                    foundFiles.add(file);
                    alreadyAdded.add(file.getId());
                }
            });

        return foundFiles;
    }

    private Collection<MediaFile> rank(final Collection<MediaFile> foundFiles, final Collection<Keyword> foundKeywords) {
        final List<Result> results = foundFiles.stream()
            .map(file -> {
                final int hitCount = foundKeywords.stream()
                    .mapToInt(keyword -> file.getKeywords().contains(keyword) ? 1 : 0)
                    .sum();
                return new Result(file, hitCount);
            }).collect(Collectors.toList());

        results.sort(Comparator.comparingInt(a -> a.hitCount));
        Collections.reverse(results);

        return results.stream()
            .map(result -> result.file)
            .collect(Collectors.toList());
    }

    @Override
    public Download downloadFile(final String relativeFilename) throws IOException {
        LOGGER.debug("Download file {}", relativeFilename);
        final MediaFile file = mediaFiles.findByRelativeFileName(relativeFilename);

        if (file == null) {
            throw new NotFound("There is no such file '%s' to download!", relativeFilename);
        }

        final Path absoluteLocalPath = Paths.get(file.getBucket().getDirectory()).resolve(file.getRelativeFileName());
        return new Download(absoluteLocalPath, file.getFormat());
    }

    private class Result {
        private final MediaFile file;
        private final int hitCount;

        Result(final MediaFile file, final int hitCount) {
            this.file = file;
            this.hitCount = hitCount;
        }
    }

}
