package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.dao.MediaDao;
import de.weltraumschaf.maconha.dao.OriginFileDao;
import java.util.Arrays;
import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;
import static org.mockito.Mockito.mock;
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
    @Ignore
    public void execute() {
        when(input.findAll()).thenReturn(Arrays.asList());
    }

}
