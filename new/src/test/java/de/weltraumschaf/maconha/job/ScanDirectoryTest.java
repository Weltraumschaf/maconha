package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.MaconhaApplication;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for {@link ScanDirectory}.
 */
@Transactional
//@Rollback(false)
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MaconhaApplication.class)
public class ScanDirectoryTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();
    private final ScanDirectory sut = new ScanDirectory();
    private @Autowired AutowireCapableBeanFactory beanFactory;

    @Before
    public void autowireDependnecies() {
        beanFactory.autowireBean(sut);
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

    @Test
    public void execute() throws Exception {
        final URI fixtures = getClass().getResource("/de/weltraumschaf/maconha/job/ScanJob").toURI();
        final Path baseDir = Paths.get(fixtures);
        sut.setBaseDir(baseDir);

        sut.execute();
    }
}
