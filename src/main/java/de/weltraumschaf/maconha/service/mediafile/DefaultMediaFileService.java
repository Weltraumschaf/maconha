package de.weltraumschaf.maconha.service.mediafile;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.model.FileMetaData;
import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.MediaFileService;
import de.weltraumschaf.maconha.service.scan.extraction.MetaDataExtractor;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Default implementation.
 */
@Service
final class DefaultMediaFileService implements MediaFileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMediaFileService.class);
    private final MediaFileRepo mediaFiles;

    @Autowired
    DefaultMediaFileService(final MediaFileRepo mediaFiles) {
        super();
        this.mediaFiles = mediaFiles;
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
}
