package de.weltraumschaf.maconha.service.search;

import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.SearchService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
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
    public Collection<MediaFile> forKeywords(final Collection<String> query) {
        Validate.notNull(query, "query");

        if (query.isEmpty()) {
            return Collections.emptyList();
        }

        LOGGER.debug("Search for {}.", query);
        final Collection<Keyword> foundKeywords = keywords.findByLiteralIn(query);
        LOGGER.debug("found {} keywords.", foundKeywords.size());
        final Collection<MediaFile> foundFiles = reduce(foundKeywords);

        return rank(foundFiles, foundKeywords);
    }

    private Collection<MediaFile> reduce(final Collection<Keyword> foundKeywords) {
        final Collection<Long> alreadyAdded = new ArrayList<>();
        final Collection<MediaFile> foundFiles = new ArrayList<>();

        foundKeywords.stream()
            .flatMap(k -> k.getMediaFiles().stream())
            .forEach(file -> {
                if (alreadyAdded.contains(file.getId())) {
                    return;
                }

                foundFiles.add(file);
                alreadyAdded.add(file.getId());
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

        return results.stream()
            .map(result -> result.file)
            .collect(Collectors.toList());
    }

    @Override
    public void downloadFile(final String relativeFilename, final HttpServletResponse response) throws IOException {
        LOGGER.debug("Download file {}", relativeFilename);
        final MediaFile file = mediaFiles.findByRelativeFileName(relativeFilename);

        if (file == null) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "There is no file " + relativeFilename);
            return;
        }

        final Path absoluteLocalPath = Paths.get(file.getBucket().getDirectory()).resolve(file.getRelativeFileName());

        try (final InputStream in = Files.newInputStream(absoluteLocalPath)) {
            copy(in, response.getOutputStream());
        }

        response.setContentType(file.getFormat().getMimeType());
        response.flushBuffer();
    }

    private void copy(final InputStream in, final OutputStream out) throws IOException {
        final byte[] buffer = new byte[1024];
        int len = in.read(buffer);

        while (len != -1) {
            out.write(buffer, 0, len);
            len = in.read(buffer);
        }
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
