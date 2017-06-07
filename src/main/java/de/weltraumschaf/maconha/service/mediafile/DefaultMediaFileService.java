package de.weltraumschaf.maconha.service.mediafile;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.model.*;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.MediaFileService;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;

/**
 * Default implementation.
 */
@Service
final class DefaultMediaFileService implements MediaFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMediaFileService.class);

    private final MediaFileRepo mediaFiles;
    private final KeywordRepo keywords;

    @Lazy
    @Autowired
    DefaultMediaFileService(final MediaFileRepo mediaFiles, final KeywordRepo keywords) {
        super();
        this.mediaFiles = mediaFiles;
        this.keywords = keywords;
    }

    @Override
    public boolean isFileUnseen(final HashedFile file, final Bucket bucket) {
        Validate.notNull(file, "file");
        Validate.notNull(bucket, "bucket");

        final MediaFile found = mediaFiles.findByRelativeFileNameAndBucket(file.getFile(), bucket);

        if (null == found) {
            LOGGER.debug("File not scanned yet: {}", file.getFile());
            return true;
        }

        if (found.getFileHash().equals(file.getHash())) {
            LOGGER.debug("File already scanned and hash not changed: {}", file.getFile());
            return false;
        }

        LOGGER.debug("File already scanned but hash changed: {}", file.getFile());
        return true;
    }

    @Override
    public FileMetaData extractFileMetaData(final Bucket bucket, final HashedFile file) {
        try {
            final Path absoluteFile = Paths.get(bucket.getDirectory()).resolve(file.getFile());
            return new MetaDataExtractor().extract(absoluteFile.toString());
        } catch (final Exception e) {
            LOGGER.warn(e.getMessage(), e);
            return FileMetaData.NOTHING;
        }
    }

    @Override
    @Transactional
    public void extractAndStoreMetaData(final Bucket bucket, final HashedFile file) {
        LOGGER.debug("Extract meta data for: {}", file.getFile());
        final FileExtension extension;

        try {
            extension = file.extractExtension();
        } catch (final IllegalArgumentException e) {
            LOGGER.warn(e.getMessage());
            LOGGER.warn("Skipping file {}", file.getFile());
            return;
        }

        final FileMetaData fileMetaData = extractFileMetaData(bucket, file);

        final MediaFile media = new MediaFile();
        media.setType(MediaType.forValue(extension));
        media.setFormat(fileMetaData.getMime());
        media.setRelativeFileName(file.getFile());
        media.setFileHash(file.getHash());
        media.setBucket(bucket);

        final Collection<String> foundKeywords = new HashSet<>();
        foundKeywords.addAll(new KeywordsFromFileNameExtractor().extract(file.getFile()));
        foundKeywords.addAll(new KeywordsFromMetaDataExtractor().extract(fileMetaData.getData()));

        foundKeywords.stream()
            .filter(new MalformedKeywords())
            .filter(new IgnoredKeywords())
            .map(literal -> {
                Keyword keyword = keywords.findByLiteral(literal);

                if (null == keyword) {
                    LOGGER.debug("Save new keyword '{}'.", literal);
                    keyword = new Keyword();
                    keyword.setLiteral(literal);
                    keywords.save(keyword);
                }

                return keyword;
            }).forEach(media::addKeyword);

        mediaFiles.save(media);
    }
}
