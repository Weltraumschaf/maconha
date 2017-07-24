package de.weltraumschaf.maconha.backend.service.scan.batch;

import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.repo.BucketRepo;
import de.weltraumschaf.maconha.backend.service.MediaFileService;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;
import org.junit.Test;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link MetaDataExtractionTasklet}.
 */
public final class MetaDataExtractionTaskletTest {

    private final BatchContextFactory contexts = new BatchContextFactory();
    private final BucketRepo buckets = mock(BucketRepo.class);
    private final MediaFileService mediaFiles = mock(MediaFileService.class);
    private final MetaDataExtractionTasklet sut = new MetaDataExtractionTasklet(buckets, mediaFiles);

    @Test
    public void execute() throws Exception {
        final Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(JobParameterKeys.BUCKET_ID, new JobParameter(42L, false));

        final HashedFile file1 = new HashedFile("hash1", "file1");
        final HashedFile file2 = new HashedFile("hash2", "file2");
        final ExecutionContext executionContext = new ExecutionContext();
        executionContext.put(ContextKeys.UNSEEN_FILES, new HashSet<>(Arrays.asList(file1, file2)));

        final Bucket bucket = new Bucket();
        when(buckets.findById(42L)).thenReturn(bucket);

        assertThat(
            sut.execute(mock(StepContribution.class),
                contexts.createContext(parameters, executionContext)), is(RepeatStatus.FINISHED));

        verify(mediaFiles, times(1)).extractAndStoreMetaData(bucket, file1);
        verify(mediaFiles, times(1)).extractAndStoreMetaData(bucket, file2);
    }

}
