package de.weltraumschaf.maconha.service.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.SystemCommandTasklet;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.concurrent.TimeUnit;

/**
 *
 */
final class FindFilesTasklet extends SystemCommandTasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterSeenFilesTasklet.class);

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        final String directory = (String) chunkContext.getStepContext().getJobParameters().get(JobParameterKeys.BUCKET_DIRECTORY);
        LOGGER.debug("Find and hash files in directory {} ...", directory);
        // FIXME Remove absolute path!
        setCommand("/Users/sst/src/private/maconha-ng/bin/dirhash " + directory);
        setTimeout(TimeUnit.MINUTES.toMillis(10L));

        return super.execute(contribution, chunkContext);
    }
}
