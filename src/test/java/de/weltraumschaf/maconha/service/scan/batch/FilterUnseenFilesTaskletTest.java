package de.weltraumschaf.maconha.service.scan.batch;

import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.MediaFileService;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link FilterUnseenFilesTasklet}.
 */
public final class FilterUnseenFilesTaskletTest {
    private final FilterUnseenFilesTasklet sut = new FilterUnseenFilesTasklet(mock(MediaFileService.class));

    @Test
    @Ignore("Write some tests")
    public void todo() {
    }
}
