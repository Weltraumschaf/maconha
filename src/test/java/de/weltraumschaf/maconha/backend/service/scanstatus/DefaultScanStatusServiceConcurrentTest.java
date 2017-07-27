package de.weltraumschaf.maconha.backend.service.scanstatus;

import de.weltraumschaf.maconha.app.MaconhaConfiguration;
import de.weltraumschaf.maconha.backend.service.ScanService;
import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Concurrency test for {@link DefaultScanStatusService}.
 */
public final class DefaultScanStatusServiceConcurrentTest extends MultithreadedTestCase {
    private final TemporaryFolder tmp = new TemporaryFolder();
    private final MaconhaConfiguration config = new MaconhaConfiguration();
    private final StatusSerializer serializer = new StatusSerializer() {
        private final Collection<ScanService.ScanStatus> data = new ArrayList<>();

        @Override
        public void serialize(final Collection<ScanService.ScanStatus> statuses, final Appendable writer) {
            data.clear();
            data.addAll(statuses);
        }

        @Override
        public Collection<ScanService.ScanStatus> deserialize(final Reader reader) {
            return data;
        }
    };
    final ScanService.ScanStatus statusOne = new ScanService.ScanStatus(
        42L,
        "bucket",
        "created",
        "started",
        "ended",
        "duration",
        "status"
    );
    private final DefaultScanStatusService sut = new DefaultScanStatusService(config, serializer);

    @Override
    public void initialize() {
        try {
            tmp.create();
            config.setHomedir(tmp.getRoot().getAbsolutePath());
            final File folder = tmp.newFolder(DefaultScanStatusService.STATUSES_DIR_NAME);
            //noinspection ResultOfMethodCallIgnored
            new File(folder, DefaultScanStatusService.STATUSES_FILE_NAME).createNewFile();
        } catch (final IOException e) {
            throw new IOError(e);
        }
    }

    public void thread1() {
        assertThat(sut.allStatuses(), is(empty()));
        waitForTick(2);
        sut.storeStatus(statusOne);
        assertTick(2);
    }

    public void thread2() {
        waitForTick(1);
        assertThat(sut.allStatuses(), is(empty()));
        waitForTick(3);
        assertThat(sut.allStatuses(), containsInAnyOrder(statusOne));
        assertTick(3);
    }

    @Override
    public void finish() {
        tmp.delete();
    }

    @Test
    public void exclusiveLocking() throws Throwable {
        TestFramework.runOnce(new DefaultScanStatusServiceConcurrentTest());
    }
}
