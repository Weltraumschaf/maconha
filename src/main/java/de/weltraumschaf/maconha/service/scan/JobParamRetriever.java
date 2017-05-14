package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;

import java.util.Set;

/**
 * Helper class to extract particular parameters from {@link ChunkContext job step chunk context}.
 */
final class JobParamRetriever {

    /**
     * Retrieve the bucket directory to scan.
     *
     * @param ctx must not be {@code null}
     * @return never {@code null}
     */
    String retrieveBucketDirectory(final ChunkContext ctx) {
        Validate.notNull(ctx, "ctx");
        final Object bucketDir = retrieveParameter(ctx, JobParameterKeys.BUCKET_DIRECTORY);

        if (bucketDir instanceof String) {
            return (String) bucketDir;
        }

        throw new IllegalArgumentException(
            String.format(
                "The job parameter '%s' was not a string as expected (was: %s)!",
                JobParameterKeys.BUCKET_DIRECTORY,
                bucketDir));
    }

    /**
     * Retrieve the bucket id to scan.
     *
     * @param ctx must not be {@code null}
     * @return never {@code null}
     */
    Long retrieveBucketId(final ChunkContext ctx) {
        Validate.notNull(ctx, "ctx");
        final Object bucketId = retrieveParameter(ctx, JobParameterKeys.BUCKET_ID);

        if (bucketId instanceof Long) {
            return (Long) bucketId;
        }

        throw new IllegalArgumentException(
            String.format(
                "The job parameter '%s' was not a long as expected (was: %s)!",
                JobParameterKeys.BUCKET_ID,
                bucketId));
    }

    void storeUnseenFiles(final ChunkContext ctx, final Set<HashedFile> unseenFiles) {
        getExecutionContext(ctx).put(ContextKeys.UNSEEN_FILES, unseenFiles);
    }

    @SuppressWarnings("unchecked")
    Set<HashedFile> retrieveUnseenFiles(final ChunkContext ctx) {
        final Object contextData = getExecutionContext(ctx).get(ContextKeys.UNSEEN_FILES);

        if (contextData instanceof Set) {
            return (Set<HashedFile>) contextData;
        }

        throw new IllegalArgumentException("Can not deal with context data: " + contextData);
    }

    private Object retrieveParameter(final ChunkContext ctx, final String parameterKey) {
        return ctx.getStepContext().getJobParameters().get(parameterKey);
    }

    private ExecutionContext getExecutionContext(final ChunkContext ctx) {
        return ctx.getStepContext()
            .getStepExecution()
            .getJobExecution()
            .getExecutionContext();
    }
}
