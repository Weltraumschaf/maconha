package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.maconha.core.FileExtension;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.repo.BucketRepo;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link MetaDataExtractionTasklet}.
 */
public final class MetaDataExtractionTaskletTest {
    private final MetaDataExtractionTasklet sut = new MetaDataExtractionTasklet(mock(BucketRepo.class), mock(MediaFileRepo.class));

    @Test
    public void extractExtension() {
        assertThat(sut.extractExtension(new HashedFile("hash", "foo.mkv")), is(FileExtension.MATROSKA_VIDEO_FILE));
        assertThat(sut.extractExtension(new HashedFile("hash", "foo.swf")), is(FileExtension.SHOCKWAVE_FLASH_MOVIE));
    }

    @Test
    public void relativizeFilename() {
        final Bucket bucket = new Bucket();
        bucket.setDirectory("/foo/bar");
        assertThat(
            sut.relativizeFilename(bucket, new HashedFile("hash", "/foo/bar/baz/snafu.avi")),
            is("baz/snafu.avi"));
    }
}
