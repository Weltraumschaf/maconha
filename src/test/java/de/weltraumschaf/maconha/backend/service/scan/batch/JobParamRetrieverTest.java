package de.weltraumschaf.maconha.backend.service.scan.batch;

import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import org.junit.Test;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link JobParamRetriever}.
 */
public final class JobParamRetrieverTest {

    private final BatchContextFactory contexts = new BatchContextFactory();
    private final JobParamRetriever sut = new JobParamRetriever();

    @Test(expected = IllegalArgumentException.class)
    public void retrieveBucketDirectory_notPresent() {
        final ChunkContext context = contexts.createContext();

        sut.retrieveBucketDirectory(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void retrieveBucketDirectory_notString() {
        final Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(JobParameterKeys.BUCKET_DIRECTORY, new JobParameter(42L, false));
        final ChunkContext context = contexts.createContext(parameters);

        sut.retrieveBucketDirectory(context);
    }

    @Test
    public void retrieveBucketDirectory() {
        final Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(JobParameterKeys.BUCKET_DIRECTORY, new JobParameter("dir", false));
        final ChunkContext context = contexts.createContext(parameters);

        assertThat(sut.retrieveBucketDirectory(context), is("dir"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void retrieveBucketId_notPresent() {
        final ChunkContext context = contexts.createContext();

        sut.retrieveBucketId(context);


    }

    @Test(expected = IllegalArgumentException.class)
    public void retrieveBucketId_NotLong() {
        final Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(JobParameterKeys.BUCKET_ID, new JobParameter("foo", false));
        final ChunkContext context = contexts.createContext(parameters);

        sut.retrieveBucketId(context);

    }

    @Test
    public void retrieveBucketId() {
        final Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(JobParameterKeys.BUCKET_ID, new JobParameter(42L, false));
        final ChunkContext context = contexts.createContext(parameters);

        assertThat(sut.retrieveBucketId(context), is(42L));

    }

    @Test
    public void storeUnseenFiles() {
        final Set<HashedFile> unseenFiles = new HashSet<>();
        unseenFiles.add(new HashedFile("hash", "file"));
        final ExecutionContext executionContext = new ExecutionContext();

        sut.storeUnseenFiles(contexts.createContext(executionContext), unseenFiles);

        assertThat(executionContext.get(ContextKeys.UNSEEN_FILES), is(unseenFiles));
    }

    @Test
    public void retrieveUnseenFiles() {
        final Set<HashedFile> unseenFiles = new HashSet<>();
        unseenFiles.add(new HashedFile("hash", "file"));
        final ExecutionContext executionContext = new ExecutionContext();
        executionContext.put(ContextKeys.UNSEEN_FILES, unseenFiles);

        assertThat(sut.retrieveUnseenFiles(contexts.createContext(executionContext)), is(unseenFiles));
    }
}
