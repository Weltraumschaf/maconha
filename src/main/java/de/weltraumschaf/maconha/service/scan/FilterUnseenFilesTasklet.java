package de.weltraumschaf.maconha.service.scan;

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

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext ctx) throws Exception {
        final String directory = retrieveBucketDirectory(ctx);
        final Path checksums = Paths.get(directory).resolve(".checksums");
        LOGGER.debug("Reading hashed files from {} ...", checksums);
        final Set<HashedFile> hashedFiles = reader.read(checksums);
        LOGGER.debug("Read {} filenames with hashes.", hashedFiles.size());
        storeResult(ctx, filterFiles(hashedFiles));

        return RepeatStatus.FINISHED;
    }

    private String retrieveBucketDirectory(final ChunkContext ctx) {
        final Object bucketDir = ctx.getStepContext().getJobParameters().get(JobParameterKeys.BUCKET_DIRECTORY);

        if (bucketDir instanceof String) {
            return (String) bucketDir;
        }

        throw new IllegalArgumentException(
            String.format(
                "The job parameter '%s' was not a string as expected (was: %s)!",
                JobParameterKeys.BUCKET_DIRECTORY,
                bucketDir));
    }

    private Set<HashedFile> filterFiles(final Set<HashedFile> hashedFiles) {
        return hashedFiles.stream()
            .filter(this::isFileUnseen)
            .collect(Collectors.toSet());
    }

    private boolean isFileUnseen(final HashedFile file) {
        // TODO Query the DB to see if the file was already added and has the same hash.
        return true;
    }

    private void storeResult(final ChunkContext ctx, final Set<HashedFile> unseenFiles) {
        LOGGER.debug("Store {} unseen files in execution context.", unseenFiles.size());
        ctx.getStepContext()
            .getStepExecution()
            .getJobExecution()
            .getExecutionContext()
            .put(ContextKeys.UNSEEN_FILES, unseenFiles);
    }

}
