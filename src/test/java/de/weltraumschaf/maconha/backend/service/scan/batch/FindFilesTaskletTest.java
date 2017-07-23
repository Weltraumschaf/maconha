package de.weltraumschaf.maconha.backend.service.scan.batch;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link FindFilesTasklet}.
 */
public final class FindFilesTaskletTest {
    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();
    private final BatchContextFactory contexts = new BatchContextFactory();

    @Test
    public void execute() throws Exception {
        final Path binDir = tmp.newFolder("bin").toPath();
        final FindFilesTasklet sut = new FindFilesTasklet(binDir.toString());

        final Path dirhash = binDir.resolve("dirhash");
        Files.copy(getClass().getResourceAsStream("/dirhash"), dirhash);
        Files.setPosixFilePermissions(dirhash, EnumSet.allOf(PosixFilePermission.class));

        final Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put(JobParameterKeys.BUCKET_ID, new JobParameter(42L, false));
        final Path bucket = tmp.newFolder("bucket").toPath();
        parameters.put(
            JobParameterKeys.BUCKET_DIRECTORY,
            new JobParameter(bucket.toString(), false));

        final ChunkContext context = contexts.createContext(parameters);

        assertThat(
            sut.execute(mock(StepContribution.class), context),
            is(RepeatStatus.FINISHED));
        assertThat(Files.exists(bucket.resolve(".checksums")), is(true));
    }
}
