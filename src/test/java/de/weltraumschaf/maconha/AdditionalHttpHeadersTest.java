package de.weltraumschaf.maconha;

import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    private static final class HttpServletResponseStub implements HttpServletResponse {
        final Map<String,String> header = new HashMap<>();

        @Override
        public void addCookie(final Cookie cookie) {

        }

        @Override
        public boolean containsHeader(final String name) {
            return false;
        }

        @Override
        public String encodeURL(final String url) {
            return null;
        }

        @Override
        public String encodeRedirectURL(final String url) {
            return null;
        }

        @Override
        public String encodeUrl(final String url) {
            return null;
        }

        @Override
        public String encodeRedirectUrl(final String url) {
            return null;
        }

        @Override
        public void sendError(final int sc, final String msg) throws IOException {

        }

        @Override
        public void sendError(final int sc) throws IOException {

        }

        @Override
        public void sendRedirect(final String location) throws IOException {

        }

        @Override
        public void setDateHeader(final String name, final long date) {

        }

        @Override
        public void addDateHeader(final String name, final long date) {

        }

        @Override
        public void setHeader(final String name, final String value) {
            header.put(name, value);
        }

        @Override
        public void addHeader(final String name, final String value) {

        }

        @Override
        public void setIntHeader(final String name, final int value) {

        }

        @Override
        public void addIntHeader(final String name, final int value) {

        }

        @Override
        public void setStatus(final int sc) {

        }

        @Override
        public void setStatus(final int sc, final String sm) {

        }

        @Override
        public int getStatus() {
            return 0;
        }

        @Override
        public String getHeader(final String name) {
            return null;
        }

        @Override
        public Collection<String> getHeaders(final String name) {
            return null;
        }

        @Override
        public Collection<String> getHeaderNames() {
            return null;
        }

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return null;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return null;
        }

        @Override
        public void setCharacterEncoding(final String charset) {

        }

        @Override
        public void setContentLength(final int len) {

        }

        @Override
        public void setContentLengthLong(final long len) {

        }

        @Override
        public void setContentType(final String type) {

        }

        @Override
        public void setBufferSize(final int size) {

        }

        @Override
        public int getBufferSize() {
            return 0;
        }

        @Override
        public void flushBuffer() throws IOException {

        }

        @Override
        public void resetBuffer() {

        }

        @Override
        public boolean isCommitted() {
            return false;
        }

        @Override
        public void reset() {

        }

        @Override
        public void setLocale(final Locale loc) {

        }

        @Override
        public Locale getLocale() {
            return null;
        }
    }
}
