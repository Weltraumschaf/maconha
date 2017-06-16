package de.weltraumschaf.maconha.service.mediafile;

import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.model.FileMetaData;
import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link DefaultMediaFileService}.
 */
public final class DefaultMediaFileServiceTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    private final HashedFile file = new HashedFile("foo", "bar");
    private final Bucket bucket = new Bucket();

    private final MediaFileRepo mediaFiles = mock(MediaFileRepo.class);
    private final KeywordRepo keywords = mock(KeywordRepo.class);
    @SuppressWarnings("unchecked")
    private Extractor<FileMetaData> extractor = mock(Extractor.class);
    private final DefaultMediaFileService sut = new DefaultMediaFileService(mediaFiles, keywords, extractor);

    @Test
    public void isFileUnseen_fileMustNotBeNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("file");

        sut.isFileUnseen(null, bucket);
    }

    @Test
    public void isFileUnseen_bucketMustNotBeNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("bucket");

        sut.isFileUnseen(file, null);
    }

    @Test
    public void isFileUnseen_noPersistedMediaFile() {
        assertThat(sut.isFileUnseen(file, bucket), is(true));
    }

    @Test
    public void isFileUnseen_persistedMediaFileWithSameHash() {
        final MediaFile media = new MediaFile();
        media.setFileHash(file.getHash());
        when(mediaFiles.findByRelativeFileNameAndBucket(file.getFile(), bucket)).thenReturn(media);

        assertThat(sut.isFileUnseen(file, bucket), is(false));
    }

    @Test
    public void isFileUnseen_persistedMediaFileWithDifferentHash() {
        final MediaFile media = new MediaFile();
        media.setFileHash("snafu");
        when(mediaFiles.findByRelativeFileNameAndBucket(file.getFile(), bucket)).thenReturn(media);

        assertThat(sut.isFileUnseen(file, bucket), is(true));
    }

    @Test
    public void extractFileMetaData() {
        final Bucket bucket = new Bucket();
        bucket.setDirectory("/foo/bar");
        final FileMetaData expected = new FileMetaData("mime", "data");
        when(extractor.extract("/foo/bar/file")).thenReturn(expected);

        assertThat(
            sut.extractFileMetaData(bucket, new HashedFile("hash", "file")),
            is(expected));
    }
}
