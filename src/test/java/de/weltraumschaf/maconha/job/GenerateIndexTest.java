package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.dao.KeywordDao;
import de.weltraumschaf.maconha.dao.MediaDao;
import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.model.OriginFile;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link GenerateIndex}.
 */
public class GenerateIndexTest {

    private final MediaDao input = mock(MediaDao.class);
    private final KeywordDao output = mock(KeywordDao.class);
    private final GenerateIndex sut = new GenerateIndex();

    @Before
    public void injectMocks() {
        sut.setInput(input);
        sut.setOutput(output);
    }

    @Test
    public void execute() throws Exception {
        final Media mediaOne = new Media(); // No file, will be ignored.
        final Media mediaTwo = new Media().setOriginFile(
            new OriginFile()
            .setBaseDir(Paths.get("/foo/bar"))
            .setAbsolutePath(Paths.get("/foo/bar/Movie_Two.avi"))
        );
        final Media mediaThree = new Media().setOriginFile(
            new OriginFile()
            .setBaseDir(Paths.get("/foo/bar"))
            .setAbsolutePath(Paths.get("/foo/bar/Movie_Three.avi"))
        );
        when(input.findAll()).thenReturn(Arrays.asList(mediaOne, mediaTwo, mediaThree));
        final Keyword movieKeyword = new Keyword().setId(42).setLiteral("movie");
        when(output.findByLiteral("movie")).thenReturn(movieKeyword);

        sut.execute();

        verify(output, times(2)).save(movieKeyword);
        verify(output, times(1)).save(new Keyword().setLiteral("two").addMedias(mediaTwo));
        verify(output, times(1)).save(new Keyword().setLiteral("three").addMedias(mediaThree));
    }

}
