package de.weltraumschaf.maconha.backend.service.scan.batch;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
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
     * @param context must not be {@code null}
     * @return never {@code null}
     */
    String retrieveBucketDirectory(final ChunkContext context) {
        Validate.notNull(context, "context");
        final Object bucketDir = retrieveParameter(context, JobParameterKeys.BUCKET_DIRECTORY);

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
     * @param context must not be {@code null}
     * @return never {@code null}
     */
    Long retrieveBucketId(final ChunkContext context) {
        Validate.notNull(context, "context");
        final Object bucketId = retrieveParameter(context, JobParameterKeys.BUCKET_ID);

        if (bucketId instanceof Long) {
            return (Long) bucketId;
        }

        throw new IllegalArgumentException(
            String.format(
                "The job parameter '%s' was not a long as expected (was: %s)!",
                JobParameterKeys.BUCKET_ID,
                bucketId));
    }

    void storeUnseenFiles(final ChunkContext context, final Set<HashedFile> unseenFiles) {
        getExecutionContext(context).put(ContextKeys.UNSEEN_FILES, unseenFiles);
    }

    @SuppressWarnings("unchecked")
    Set<HashedFile> retrieveUnseenFiles(final ChunkContext context) {
        final Object contextData = getExecutionContext(context).get(ContextKeys.UNSEEN_FILES);

        if (contextData instanceof Set) {
            return (Set<HashedFile>) contextData;
        }

        throw new IllegalArgumentException("Can not deal with context data: " + contextData);
    }

    private Object retrieveParameter(final ChunkContext context, final String parameterName) {
        return context.getStepContext().getJobParameters().get(parameterName);
    }

    private ExecutionContext getExecutionContext(final ChunkContext context) {
        return context.getStepContext()
            .getStepExecution()
            .getJobExecution()
            .getExecutionContext();
    }
}
