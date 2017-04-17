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
 *
 */
final class FilterSeenFilesTasklet implements Tasklet {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterSeenFilesTasklet.class);
    private final HashFileReader reader = new HashFileReader();

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        final String directory = (String) chunkContext.getStepContext().getJobParameters().get(JobParameterKeys.BUCKET_DIRECTORY);
        final Path checksums = Paths.get(directory).resolve(".checksums");
        LOGGER.debug("Reading hashed files from {} ...", checksums);
        final Set<HashedFile> hashedFiles = reader.read(checksums);
        LOGGER.debug("Read {} filenames with hashes.", hashedFiles.size());
        final Set<HashedFile> unseenFiles = hashedFiles.stream()
            .filter(this::isFileUnseen)
            .collect(Collectors.toSet());
        return RepeatStatus.FINISHED;
    }

    private boolean isFileUnseen(final HashedFile file) {
        return true;
    }
}
