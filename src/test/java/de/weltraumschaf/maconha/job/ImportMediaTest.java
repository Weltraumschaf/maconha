package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.core.FileExtension;
import de.weltraumschaf.maconha.dao.MediaDao;
import de.weltraumschaf.maconha.dao.OriginFileDao;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.model.OriginFile;
import java.nio.file.Paths;
import java.util.Arrays;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.LocalDateTime;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ImportMedia}.
 */
public class ImportMediaTest {

    private final OriginFileDao input = mock(OriginFileDao.class);
    private final MediaDao output = mock(MediaDao.class);
    private final ImportMedia sut = new ImportMedia();

    @Before
    public void injectMocks() {
        sut.setInput(input);
        sut.setOutput(output);
    }

    @Test
    public void execute() throws Exception {
        final OriginFile fileOne = new OriginFile()
            .setId(1)
            .setBaseDir(Paths.get("/foo/movies"))
            .setAbsolutePath(Paths.get("/foo/movies/file_one.avi"))
            .setFingerprint(DigestUtils.sha256Hex("fileOne"))
            .setScanTime(new LocalDateTime());
        final OriginFile fileTwo = new OriginFile()
            .setId(2)
            .setBaseDir(Paths.get("/foo/movies"))
            .setAbsolutePath(Paths.get("/foo/movies/file_two.mov"))
            .setFingerprint(DigestUtils.sha256Hex("fileTwo"))
            .setScanTime(new LocalDateTime())
            .setImported(new Media());
        final OriginFile fileThree = new OriginFile()
            .setId(3)
            .setBaseDir(Paths.get("/foo/movies"))
            .setAbsolutePath(Paths.get("/foo/movies/file_three.mpg"))
            .setFingerprint(DigestUtils.sha256Hex("fileThree"))
            .setScanTime(new LocalDateTime());
        when(input.findAll()).thenReturn(Arrays.asList(fileOne, fileTwo, fileThree));

        sut.execute();

        verify(output, times(1)).save(new Media()
            .setTitle("file one")
            .setType(Media.MediaType.VIDEO)
            .setFormat(FileExtension.AUDIO_VIDEO_INTERLEAVE)
            .setLastImported(sut.getImportTime())
            .setOriginFile(fileOne));
        verify(output, times(1)).save(new Media()
            .setTitle("file three")
            .setType(Media.MediaType.VIDEO)
            .setFormat(FileExtension.MPEG_VIDEO_FILE)
            .setLastImported(sut.getImportTime())
            .setOriginFile(fileThree));
    }

}
