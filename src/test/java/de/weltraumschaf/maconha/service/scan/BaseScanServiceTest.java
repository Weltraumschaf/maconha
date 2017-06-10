package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link BaseScanService}.
 */
public final class BaseScanServiceTest {
    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    private final MaconhaConfiguration config = new MaconhaConfiguration();
    private final StatusSerializer serializer = mock(StatusSerializer.class);
    private final BaseScanService sut = new BaseScanService(config, serializer) {
    };

    @Before
    public void setHomeDirToConfig() {
        config.setHomedir(tmp.getRoot().getAbsolutePath());
    }

    @Test
    public void init_callsInitHook() {
        final BaseScanService spy = spy(sut);

        spy.init();

        verify(spy, times(1)).initHook();
    }

    @Test
    public void init_readStatuses_fileDoesNotExist() {
        sut.init();

        assertThat(sut.statuses, is(empty()));
    }

    @Test
    public void deinit_callsDeinitHook() {
        final BaseScanService spy = spy(sut);

        spy.deinit();

        verify(spy, times(1)).deinitHook();
    }
}
