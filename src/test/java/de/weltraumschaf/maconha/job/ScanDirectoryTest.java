package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.service.MediaService;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link ScanDirectory}.
 */
public class ScanDirectoryTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();
    private final MediaService service = mock(MediaService.class);
    private final ScanDirectory sut = new ScanDirectory();

    @Before
    public void injectMocks() {
        sut.setService(service);
    }

    @Test
    public void execute() throws Exception {
        sut.setBaseDir("/tmp");

        assertThat(sut.execute(), is(nullValue()));

        verify(service, times(1)).scanDirecotry(sut.monitor(), Paths.get("/tmp"));
    }

    @Test
    public void execute_baseDirNotSet() throws Exception {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Base dir is not set! Call #setBaseDir(Path) first.");

        sut.execute();
    }

    @Test
    public void execute_baseDirDoesNotExist() throws Exception {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Base dir '/foo/bar' does not exist!");
        sut.setBaseDir("/foo/bar");

        sut.execute();
    }

    @Test
    public void execute_baseDirIsNotReadable() throws Exception {
        final Path baseDir = tmp.newFolder().toPath();
        baseDir.toFile().setExecutable(false);
        baseDir.toFile().setWritable(false);
        baseDir.toFile().setReadable(false);
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(String.format("Base dir '%s' is not readable!", baseDir));
        sut.setBaseDir(baseDir.toString());

        sut.execute();
    }

    @Test
    public void execute_baseDirIsNotADirectory() throws Exception {
        final Path baseDir = tmp.newFile().toPath();
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(String.format("Base dir '%s' is not a directory!", baseDir));
        sut.setBaseDir(baseDir.toString());

        sut.execute();
    }

}
