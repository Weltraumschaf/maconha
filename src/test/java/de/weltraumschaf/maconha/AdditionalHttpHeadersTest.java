package de.weltraumschaf.maconha;

import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link AdditionalHttpHeaders}.
 */
public final class AdditionalHttpHeadersTest {
    private final HttpServletResponseStub response = new HttpServletResponseStub();
    private final MaconhaConfiguration config = new MaconhaConfiguration();
    private final AdditionalHttpHeaders sut = new AdditionalHttpHeaders(config);

    @Before
    public void configureVersion() {
        config.setVersion("snafu");
    }

    @Test
    public void doFilter() throws IOException, ServletException {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final FilterChain chain = mock(FilterChain.class);

        sut.doFilter(request, response, chain);

        assertThat(response.header, hasEntry("X-Maconha-Version", "snafu"));
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void addVersionHeader() {
        sut.addVersionHeader(mock(HttpServletRequest.class), response);

        assertThat(response.header, hasEntry("X-Maconha-Version", "snafu"));
    }

    @Test
    public void addCorsHeader() {
        sut.addCorsHeader(mock(HttpServletRequest.class), response);

        assertThat(response.header, hasEntry("Access-Control-Allow-Origin", "*"));
        assertThat(response.header, hasEntry("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE"));
        assertThat(response.header, hasEntry("Access-Control-Max-Age", "3600"));
        assertThat(response.header, hasEntry("Access-Control-Allow-Headers", "x-requested-with, Content-Type"));
    }

    @Test
    public void addCspHeader() {
        sut.addCspHeader(mock(HttpServletRequest.class), response);

        assertThat(response.header, hasEntry("Content-Security-Policy", "default-src 'self'; object-src 'none'"));
        assertThat(response.header, hasEntry("X-Content-Security-Policy", "default-src 'self'; object-src 'none'"));
    }

    private static final class HttpServletResponseStub extends HttpServletResponseWrapper {
        final Map<String, String> header = new HashMap<>();

        HttpServletResponseStub() {
            super(mock(HttpServletResponse.class));
        }

        @Override
        public void setHeader(final String name, final String value) {
            header.put(name, value);
        }

    }
}
