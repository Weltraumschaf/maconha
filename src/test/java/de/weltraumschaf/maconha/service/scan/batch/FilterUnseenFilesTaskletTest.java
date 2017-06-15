package de.weltraumschaf.maconha.service.scan.batch;

import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.MediaFileService;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link FilterUnseenFilesTasklet}.
 */
public final class FilterUnseenFilesTaskletTest {
    private final BatchContextFactory contexts = new BatchContextFactory();
    private final MediaFileService mediaFiles = mock(MediaFileService.class);
    private final FilterUnseenFilesTasklet sut = new FilterUnseenFilesTasklet(mediaFiles);

    @Test
    @Ignore("Write some tests")
    public void execute() throws Exception {
        final Map<String, JobParameter> parameters = new HashMap<>();
        final ExecutionContext executionContext = new ExecutionContext();

        assertThat(
            sut.execute(mock(StepContribution.class),
                contexts.createContext(parameters, executionContext)), is(RepeatStatus.FINISHED));
    }

    @Test
    public void createBucketFromContext() {
        final Bucket expected = new Bucket();
        expected.setId(42L);
        expected.setDirectory("dir");

        final Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(JobParameterKeys.BUCKET_ID, new JobParameter(expected.getId(), false));
        parameters.put(JobParameterKeys.BUCKET_DIRECTORY, new JobParameter(expected.getDirectory(), false));

        assertThat(sut.createBucketFromContext(contexts.createContext(parameters)), is(expected));
    }

    @Test
    public void filterFiles() {
        final Bucket bucket = new Bucket();
        bucket.setDirectory("/absolute/dir");
        final HashedFile file1 = new HashedFile("hash1", "/absolute/dir/file1");
        final HashedFile file2 = new HashedFile("hash2", "/absolute/dir/file2");
        final HashedFile file3 = new HashedFile("hash3", "/absolute/dir/file3");

        when(mediaFiles.isFileUnseen(new HashedFile("hash1", "file1"), bucket)).thenReturn(true);
        when(mediaFiles.isFileUnseen(new HashedFile("hash2", "file2"), bucket)).thenReturn(false);
        when(mediaFiles.isFileUnseen(new HashedFile("hash3", "file3"), bucket)).thenReturn(true);
        assertThat(
            sut.filterFiles(new HashSet<>(Arrays.asList(file1, file2, file3)), bucket),
            containsInAnyOrder(
                new HashedFile("hash1", "file1"),
                new HashedFile("hash3", "file3")
            ));
    }
}
