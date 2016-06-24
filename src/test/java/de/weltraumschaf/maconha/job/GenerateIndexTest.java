package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.service.MediaService;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link GenerateIndex}.
 */
public class GenerateIndexTest {

    private final MediaService service = mock(MediaService.class);
    private final GenerateIndex sut = new GenerateIndex();

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