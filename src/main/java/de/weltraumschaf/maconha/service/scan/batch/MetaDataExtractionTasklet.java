package de.weltraumschaf.maconha.service.scan.batch;

import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.repo.BucketRepo;
import de.weltraumschaf.maconha.service.MediaFileService;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Set;

/**
 * Tasklet to extract metadata from found files.
 */
final class MetaDataExtractionTasklet implements Tasklet {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetaDataExtractionTasklet.class);

    private final JobParamRetriever params = new JobParamRetriever();
    private final BucketRepo buckets;
    private final MediaFileService mediaFiles;

    MetaDataExtractionTasklet(final BucketRepo buckets, final MediaFileService mediaFile) {
        super();
        this.buckets = buckets;
        this.mediaFiles = mediaFile;
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext ctx) throws Exception {
        final Set<HashedFile> unseenFiles = params.retrieveUnseenFiles(ctx);
        LOGGER.debug("Received {} hashed files to extract meta data.", unseenFiles.size());
        final Bucket bucket = buckets.findById(params.retrieveBucketId(ctx));
        unseenFiles.forEach(file -> mediaFiles.extractAndStoreMetaData(bucket, file));
        return RepeatStatus.FINISHED;
    }

}
