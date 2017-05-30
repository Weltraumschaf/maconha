package de.weltraumschaf.maconha.service.scan.batch;

import de.weltraumschaf.maconha.repo.BucketRepo;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.MediaFileService;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link MetaDataExtractionTasklet}.
 */
public final class MetaDataExtractionTaskletTest {

    private final MetaDataExtractionTasklet sut = new MetaDataExtractionTasklet(
        mock(BucketRepo.class),
        mock(MediaFileRepo.class),
        mock(KeywordRepo.class),
        mock(MediaFileService.class));

    @Test
    @Ignore("TODO Write some tests.")
    public void todo() {
    }

}
