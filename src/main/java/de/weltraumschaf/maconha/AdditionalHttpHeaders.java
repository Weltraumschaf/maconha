package de.weltraumschaf.maconha;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet filter adds some additional HTTP headers to the response.
 */
@Component
public final class AdditionalHttpHeaders implements Filter {
    /**
     * This is the standard header name.
     */
    private static final String CSP_HEADER_NAME = "Content-Security-Policy";
    /**
     * Internet Explorer 10/11 expects this old version of the name.
     */
    private static final String CSP_HEADER_NAME_IE = "X-Content-Security-Policy";
    /**
     * See <a href="https://www.innoq.com/de/blog/content-security-policy-header/">this article</a> for more
     * information.
     */
    private static final String CSP_POLICY = "default-src 'self'; object-src 'none'";

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // Nothing to do here.
    }

    @Override
    public void doFilter(
        final ServletRequest request,
        final ServletResponse response,
        final FilterChain chain) throws IOException, ServletException {
        addCspHeader(request, response);
        chain.doFilter(request, response);
    }

    private void addCspHeader(final ServletRequest request, final ServletResponse response) {
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader(CSP_HEADER_NAME, CSP_POLICY);
        httpResponse.setHeader(CSP_HEADER_NAME_IE, CSP_POLICY);
    }

    @Override
    public void destroy() {
        // Nothing to do here.
    }
}
