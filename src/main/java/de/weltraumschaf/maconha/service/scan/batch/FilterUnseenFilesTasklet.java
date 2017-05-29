package de.weltraumschaf.maconha.service.scan.batch;

import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.scan.hashing.HashFileReader;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This tasklet filters the files which are not seen yet.
 */
final class FilterUnseenFilesTasklet implements Tasklet {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterUnseenFilesTasklet.class);
    private final HashFileReader reader = new HashFileReader();
    private final JobParamRetriever params = new JobParamRetriever();

    private final MediaFileRepo mediaFiles;

    FilterUnseenFilesTasklet(final MediaFileRepo mediaFiles) {
        super();
        this.mediaFiles = mediaFiles;
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext ctx) throws Exception {
        final Bucket bucket = createBucketFromContext(ctx);
        final Path checksums = Paths.get(bucket.getDirectory()).resolve(".checksums");
        LOGGER.debug("Reading hashed files from {} ...", checksums);
        final Set<HashedFile> hashedFiles = reader.read(checksums);
        LOGGER.debug("Read {} filenames with hashes.", hashedFiles.size());
        params.storeUnseenFiles(ctx, filterFiles(hashedFiles, bucket));

        return RepeatStatus.FINISHED;
    }

    private Bucket createBucketFromContext(final ChunkContext ctx) {
        final Bucket bucket = new Bucket();
        bucket.setDirectory(params.retrieveBucketDirectory(ctx));
        bucket.setId(params.retrieveBucketId(ctx));
        return bucket;
    }

    private Set<HashedFile> filterFiles(final Set<HashedFile> hashedFiles, final Bucket bucket) {
        return hashedFiles.stream()
            .map(hashedFile -> hashedFile.relativizeFilename(bucket))
            .filter(hashedFile -> isFileUnseen(hashedFile, bucket))
            .collect(Collectors.toSet());
    }

    private boolean isFileUnseen(final HashedFile file, final Bucket bucket) {
        // TODO Remove duplicated code.
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

}
