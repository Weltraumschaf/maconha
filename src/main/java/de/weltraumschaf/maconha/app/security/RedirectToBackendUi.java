package de.weltraumschaf.maconha.app.security;

import com.vaadin.spring.annotation.SpringComponent;
import de.weltraumschaf.maconha.app.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Redirects to the application after successful authentication.
 */
@SpringComponent
@ApplicationScope
public class RedirectToBackendUi implements AuthenticationSuccessHandler {

    private final ServletContext servletContext;

    @Autowired
    public RedirectToBackendUi(final ServletContext servletContext) {
        super();
        this.servletContext = servletContext;
    }

    String generateAbsoluteUrl(final String url) {
        Objects.requireNonNull(url);
        String relativeUrl = url.trim();

        if (relativeUrl.isEmpty()) {
            return servletContext.getContextPath();
        }

        relativeUrl = removeLeadingSlash(relativeUrl);
        return servletContext.getContextPath() + "/" + relativeUrl;
    }

    String removeLeadingSlash(final String url) {
        Objects.requireNonNull(url);
        return url.startsWith("/")
            ? url.substring(1)
            : url;
    }

    @Override
    public void onAuthenticationSuccess(
        final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication)
        throws IOException, ServletException {
        response.sendRedirect(generateAbsoluteUrl(Application.ADMIN_URL));
    }

}
