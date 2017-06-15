package de.weltraumschaf.maconha.service.scan.batch;

import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import org.junit.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link JobParamRetriever}.
 */
public final class JobParamRetrieverTest {

    private final JobParamRetriever sut = new JobParamRetriever();

    private ChunkContext createContext() {
        return createContext(Collections.emptyMap());
    }

    private ChunkContext createContext(final ExecutionContext executionContext) {
        return createContext(Collections.emptyMap(), executionContext);
    }

    private ChunkContext createContext(final Map<String, JobParameter> parameters) {
        return createContext(parameters, new ExecutionContext());
    }

    private ChunkContext createContext(final Map<String, JobParameter> parameters, final ExecutionContext executionContext) {
        final JobParameters jobParameters = new JobParameters(parameters);
        final JobExecution jobExecution = new JobExecution(new JobInstance(42L, "job"), 42L, jobParameters, "job");
        jobExecution.setExecutionContext(executionContext);
        final StepExecution stepExecution = new StepExecution("name", jobExecution);
        final StepContext stepContext = new StepContext(stepExecution);
        return new ChunkContext(stepContext);
    }

    @Test(expected = IllegalArgumentException.class)
    public void retrieveBucketDirectory_notPresent() {
        final ChunkContext context = createContext();

        sut.retrieveBucketDirectory(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void retrieveBucketDirectory_notString() {
        final Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(JobParameterKeys.BUCKET_DIRECTORY, new JobParameter(42L, false));
        final ChunkContext context = createContext(parameters);

        sut.retrieveBucketDirectory(context);
    }

    @Test
    public void retrieveBucketDirectory() {
        final Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(JobParameterKeys.BUCKET_DIRECTORY, new JobParameter("dir", false));
        final ChunkContext context = createContext(parameters);

        assertThat(sut.retrieveBucketDirectory(context), is("dir"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void retrieveBucketId_notPresent() {
        final ChunkContext context = createContext();

        sut.retrieveBucketId(context);


    }

    @Test(expected = IllegalArgumentException.class)
    public void retrieveBucketId_NotLong() {
        final Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(JobParameterKeys.BUCKET_ID, new JobParameter("foo", false));
        final ChunkContext context = createContext(parameters);

        sut.retrieveBucketId(context);

    }

    @Test
    public void retrieveBucketId() {
        final Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(JobParameterKeys.BUCKET_ID, new JobParameter(42L, false));
        final ChunkContext context = createContext(parameters);

        assertThat(sut.retrieveBucketId(context), is(42L));

    }

    @Test
    public void storeUnseenFiles() {
        final Set<HashedFile> unseenFiles = new HashSet<>();
        unseenFiles.add(new HashedFile("hash", "file"));
        final ExecutionContext executionContext = new ExecutionContext();

        sut.storeUnseenFiles(createContext(executionContext), unseenFiles);

        assertThat(executionContext.get(ContextKeys.UNSEEN_FILES), is(unseenFiles));
    }

    @Test
    public void retrieveUnseenFiles() {
        final Set<HashedFile> unseenFiles = new HashSet<>();
        unseenFiles.add(new HashedFile("hash", "file"));
        final ExecutionContext executionContext = new ExecutionContext();
        executionContext.put(ContextKeys.UNSEEN_FILES, unseenFiles);

        assertThat(sut.retrieveUnseenFiles(createContext(executionContext)), is(unseenFiles));
    }
}
