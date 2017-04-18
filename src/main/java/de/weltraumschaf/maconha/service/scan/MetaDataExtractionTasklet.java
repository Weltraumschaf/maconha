package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.maconha.core.FileExtension;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.model.MediaType;
import de.weltraumschaf.maconha.repo.BucketRepo;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 *
 */
final class MetaDataExtractionTasklet implements Tasklet {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetaDataExtractionTasklet.class);
    private final JobParamRetriever params = new JobParamRetriever();

    private final BucketRepo buckets;
    private final MediaFileRepo mediaFiles;

    MetaDataExtractionTasklet(final BucketRepo buckets, final MediaFileRepo mediaFiles) {
        super();
        this.buckets = buckets;
        this.mediaFiles = mediaFiles;
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext ctx) throws Exception {
        final Set<HashedFile> unseenFiles = retrieveUnseenFiles(ctx);
        LOGGER.debug("Received {} hashed files to extract meta data.", unseenFiles.size());
        final Bucket bucket = buckets.findById(params.retrieveBucketId(ctx));
        unseenFiles.forEach(file -> extractMetaData(bucket, file));
        return RepeatStatus.FINISHED;
    }

    @SuppressWarnings("unchecked")
    private Set<HashedFile> retrieveUnseenFiles(final ChunkContext ctx) {
        LOGGER.debug("Retrieve unseen files from execution context.");
        final Object contextData = ctx.getStepContext()
            .getStepExecution()
            .getJobExecution()
            .getExecutionContext()
            .get(ContextKeys.UNSEEN_FILES);

        if (contextData instanceof Set) {
            return (Set<HashedFile>) contextData;
        }

        throw new IllegalArgumentException("Can not deal with context data: " + contextData);
    }

    private void extractMetaData(final Bucket bucket, final HashedFile file) {
        LOGGER.debug("Extract meta data for: {}", file);
        final FileExtension extension;

        try {
            extension = extractExtension(file);
        } catch (final IllegalArgumentException e) {
            LOGGER.warn(e.getMessage());
            LOGGER.warn("Skipping file {}", file.getFile());
            return;
        }

        final MediaFile media = new MediaFile();
        media.setType(MediaType.forValue(extension));
        media.setFormat(extension);
        media.setRelativeFileName(relativizeFilename(bucket, file));
        media.setFileHash(file.getHash());
        media.setBucket(bucket);

        mediaFiles.save(media);
    }

    String relativizeFilename(final Bucket bucket, final HashedFile file) {
        return file.getFile().replace(bucket.getDirectory(), "").substring(1);
    }

    FileExtension extractExtension(final HashedFile file) {
        return FileExtension.forValue(FileExtension.extractExtension(file.getFile()));
    }
}
