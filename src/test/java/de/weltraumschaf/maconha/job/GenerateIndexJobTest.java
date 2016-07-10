package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.service.MediaService;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.times;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link GenerateIndexJob}.
 */
public class GenerateIndexJobTest {

    private final MediaService service = mock(MediaService.class);
    private final GenerateIndexJob sut = new GenerateIndexJob();

    @Before
    public void injectMocks() {
        sut.setService(service);
    }

    @Test
    public void execute() throws Exception {
        assertThat(sut.execute(), is(nullValue()));

        verify(service, times(1)).generateIndex(sut.monitor());
    }

}
