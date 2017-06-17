package de.weltraumschaf.maconha.service.scan.batch;

import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.service.MediaFileService;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link FilterUnseenFilesTasklet}.
 */
public final class FilterUnseenFilesTaskletTest {
    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    private final BatchContextFactory contexts = new BatchContextFactory();
    private final MediaFileService mediaFiles = mock(MediaFileService.class);
    private final FilterUnseenFilesTasklet sut = new FilterUnseenFilesTasklet(mediaFiles);

    @Test
    public void execute() throws Exception {
        final Path checksums = tmp.newFile(FilterUnseenFilesTasklet.CHECKSUMS_FILENAME).toPath();
        Files.write(
            checksums,
            Arrays.asList(
                "2f04335a0b0a96b50f9e19de9fa5a4aac2c0cc42e474bf3b9401790e33e166cb  Animation/android_207_HQ.mp4",
                "37055372aa9ab1679aeab43d9534fe65d0b217c84679b1ff43274e2b4a58a308  Animation/animusic/Animusic-AcousticCurves.wmv",
                "3a77f9b6e3aa5fed770f94b3aadcd284c8d1f8dc0ced62a018ab6671ab73c8f9  Animation/animusic/Animusic-AquaHarp.wmv")
        );

        when(mediaFiles.isFileUnseen(any(HashedFile.class), any(Bucket.class))).thenReturn(true);

        final Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(JobParameterKeys.BUCKET_ID, new JobParameter(42L, false));
        parameters.put(
            JobParameterKeys.BUCKET_DIRECTORY,
            new JobParameter(tmp.getRoot().getAbsolutePath(), false));
        final ChunkContext context = contexts.createContext(parameters);

        assertThat(
            sut.execute(mock(StepContribution.class), context),
            is(RepeatStatus.FINISHED));

        assertThat(
            new JobParamRetriever().retrieveUnseenFiles(context),
            containsInAnyOrder(
                new HashedFile("2f04335a0b0a96b50f9e19de9fa5a4aac2c0cc42e474bf3b9401790e33e166cb", "Animation/android_207_HQ.mp4"),
                new HashedFile("37055372aa9ab1679aeab43d9534fe65d0b217c84679b1ff43274e2b4a58a308", "Animation/animusic/Animusic-AcousticCurves.wmv"),
                new HashedFile("3a77f9b6e3aa5fed770f94b3aadcd284c8d1f8dc0ced62a018ab6671ab73c8f9", "Animation/animusic/Animusic-AquaHarp.wmv")
            ));
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
