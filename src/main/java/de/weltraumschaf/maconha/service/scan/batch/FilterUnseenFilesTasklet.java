package de.weltraumschaf.maconha.service.scan.batch;

import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.service.MediaFileService;
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
    static final String CHECKSUMS_FILENAME = ".checksums";
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterUnseenFilesTasklet.class);

    private final HashFileReader reader = new HashFileReader();
    private final JobParamRetriever params = new JobParamRetriever();

    private final MediaFileService mediaFiles;

    FilterUnseenFilesTasklet(final MediaFileService mediaFiles) {
        super();
        this.mediaFiles = mediaFiles;
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext ctx) throws Exception {
        final Bucket bucket = createBucketFromContext(ctx);
        final Path checksums = Paths.get(bucket.getDirectory()).resolve(CHECKSUMS_FILENAME);
        LOGGER.debug("Reading hashed files from {} ...", checksums);
        final Set<HashedFile> hashedFiles = reader.read(checksums);
        LOGGER.debug("Read {} filenames with hashes.", hashedFiles.size());
        params.storeUnseenFiles(ctx, filterFiles(hashedFiles, bucket));

        return RepeatStatus.FINISHED;
    }

    Bucket createBucketFromContext(final ChunkContext ctx) {
        final Bucket bucket = new Bucket();
        bucket.setDirectory(params.retrieveBucketDirectory(ctx));
        bucket.setId(params.retrieveBucketId(ctx));
        return bucket;
    }

    Set<HashedFile> filterFiles(final Set<HashedFile> hashedFiles, final Bucket bucket) {
        return hashedFiles.stream()
            .map(hashedFile -> hashedFile.relativizeFilename(bucket))
            .filter(relativeHashedFile -> mediaFiles.isFileUnseen(relativeHashedFile, bucket))
            .collect(Collectors.toSet());
    }

}
