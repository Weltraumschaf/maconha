package de.weltraumschaf.maconha.job;

import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link ScanDirectory}.
 */
public class ScanDirectoryTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
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
    @Ignore
    public void execute_baseDirIsNotReadable() throws Exception {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Base dir '%s' is not readable!");

        sut.execute();
    }

    @Test
    @Ignore
    public void execute_baseDirIsNotADirectory() throws Exception {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Base dir '%s' is not a directory!");

        sut.execute();
    }
}
