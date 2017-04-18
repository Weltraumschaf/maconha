package de.weltraumschaf.maconha.service.scan;

import org.springframework.batch.core.scope.context.ChunkContext;

/**
 *
 */
final class JobParamRetriever {
    String retrieveBucketDirectory(final ChunkContext ctx) {
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

    Long retrieveBucketId(final ChunkContext ctx) {
        final Object bucketId = ctx.getStepContext().getJobParameters().get(JobParameterKeys.BUCKET_ID);

        if (bucketId instanceof Long) {
            return (Long) bucketId;
        }

        throw new IllegalArgumentException(
            String.format(
                "The job parameter '%s' was not a long as expected (was: %s)!",
                JobParameterKeys.BUCKET_ID,
                bucketId));
    }
}
