package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.maconha.model.FileExtension;
import de.weltraumschaf.maconha.repo.BucketRepo;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link MetaDataExtractionTasklet}.
 */
public final class MetaDataExtractionTaskletTest {
    private final MetaDataExtractionTasklet sut = new MetaDataExtractionTasklet(
        mock(BucketRepo.class), mock(MediaFileRepo.class), mock(KeywordRepo.class));

    @Test
    public void extractExtension() {
        assertThat(sut.extractExtension(new HashedFile("hash", "foo.mkv")), is(FileExtension.MATROSKA_VIDEO));
        assertThat(sut.extractExtension(new HashedFile("hash", "foo.swf")), is(FileExtension.SHOCKWAVE_FLASH));
    }

}
