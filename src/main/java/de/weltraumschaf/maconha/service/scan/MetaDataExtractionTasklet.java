package de.weltraumschaf.maconha.service.scan;

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

    @Autowired
    private MediaFileRepo repo;

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext ctx) throws Exception {
        final Set<HashedFile> unseenFiles = retrieveUnseenfiles(ctx);
        LOGGER.debug("Received {} hashed files to extract meta data.", unseenFiles.size());
        unseenFiles.forEach(this::extractMetaData);
        return RepeatStatus.FINISHED;
    }


    @SuppressWarnings("unchecked")
    private Set<HashedFile> retrieveUnseenfiles(final ChunkContext ctx) {
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

    private void extractMetaData(final HashedFile file) {
        LOGGER.debug("Extract meta data for: {}", file);
    }
}
