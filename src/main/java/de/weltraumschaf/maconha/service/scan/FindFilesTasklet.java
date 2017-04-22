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
    private final JobParamRetriever params = new JobParamRetriever();
    private final String binDir;

    public FindFilesTasklet(final String binDir) {
        super();
        this.binDir = binDir;
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext ctx) throws Exception {
        final String bucketDirectory = params.retrieveBucketDirectory(ctx);
        LOGGER.debug("Find and hash files in bucketDirectory {} ...", bucketDirectory);
        LOGGER.debug("Using bin dir '{}'.", binDir);
        setCommand(binDir + "/dirhash " + bucketDirectory);
        setTimeout(TimeUnit.MINUTES.toMillis(10L));

        return super.execute(contribution, ctx);
    }



}
