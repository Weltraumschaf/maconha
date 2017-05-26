package de.weltraumschaf.maconha.service.scan.batch;

import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link FilterUnseenFilesTasklet}.
 */
public final class FilterUnseenFilesTaskletTest {
    private final FilterUnseenFilesTasklet sut = new FilterUnseenFilesTasklet(mock(MediaFileRepo.class));

    @Test
    public void relativizeFilename() {
        final Bucket bucket = new Bucket();
        bucket.setDirectory("/foo/bar");
        assertThat(
            sut.relativizeFilename(new HashedFile("hash", "/foo/bar/baz/snafu.avi"), bucket),
            is(new HashedFile("hash", "baz/snafu.avi")));
    }
}
