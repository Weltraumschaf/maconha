package de.weltraumschaf.maconha.job;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link ScanDirectory}.
 */
public class ScanDirectoryTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();
    private final ScanDirectory sut = new ScanDirectory();

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
        sut.setBaseDir(Paths.get("/foo/bar"));

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
        sut.setBaseDir(baseDir);

        sut.execute();
    }

    @Test
    public void execute_baseDirIsNotADirectory() throws Exception {
        final Path baseDir = tmp.newFile().toPath();
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(String.format("Base dir '%s' is not a directory!", baseDir));
        sut.setBaseDir(baseDir);

        sut.execute();
    }
}
