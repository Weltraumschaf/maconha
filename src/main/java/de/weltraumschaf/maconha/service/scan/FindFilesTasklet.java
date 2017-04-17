package de.weltraumschaf.maconha.service.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.SystemCommandTasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.concurrent.TimeUnit;

/**
 * This tasklet executes a shell script to find all files and calculates a hash of them for a given bucket.
 * <p>
 * The {@link SystemCommandTasklet} is not used directly but as extended version here because we need to add arbitrary
 * arguments to the executed command at runtime (the directory to find files in. The original implementation of {@link
 * SystemCommandTasklet} does not allow that.
 * </p>
 */
final class FindFilesTasklet extends SystemCommandTasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterUnseenFilesTasklet.class);

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext ctx) throws Exception {
        final String directory = retrieveBucketDirectory(ctx);
        LOGGER.debug("Find and hash files in directory {} ...", directory);
        // FIXME Remove absolute path!
        setCommand("/Users/sst/src/private/maconha-ng/bin/dirhash " + directory);
        setTimeout(TimeUnit.MINUTES.toMillis(10L));

        return super.execute(contribution, ctx);
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

}
