package de.weltraumschaf.maconha.service.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 *
 */
final class FindFilesTasklet implements Tasklet {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindFilesTasklet.class);

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        LOGGER.debug("<<FindFilesTasklet>>");
        return RepeatStatus.FINISHED;
    }
}
