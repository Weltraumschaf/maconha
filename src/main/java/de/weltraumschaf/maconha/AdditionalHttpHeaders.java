package de.weltraumschaf.maconha;

import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final MaconhaConfiguration config;

    @Autowired
    public AdditionalHttpHeaders(final MaconhaConfiguration config) {
        super();
        this.config = config;
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // Nothing to do here.
    }

    @Override
    public void doFilter(final ServletRequest request,  final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        addVersionHeader(request, response);
        chain.doFilter(request, response);
    }

    void addVersionHeader(final ServletRequest request, final ServletResponse response) {
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("X-Maconha-Version", config.getVersion());
    }

    // May be we need this.
    void addCorsHeader(final ServletRequest request, final ServletResponse response) {
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        httpResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type");

    }

    void addCspHeader(final ServletRequest request, final ServletResponse response) {
        // FIXME Vaadin does not work with that!
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader(CSP_HEADER_NAME, CSP_POLICY);
        httpResponse.setHeader(CSP_HEADER_NAME_IE, CSP_POLICY);
    }

    @Override
    public void destroy() {
        // Nothing to do here.
    }
}
