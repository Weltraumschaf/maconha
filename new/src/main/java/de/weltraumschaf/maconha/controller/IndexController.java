package de.weltraumschaf.maconha.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Top level controller.
 */
@Controller
public final class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(final HttpServletRequest request, Map<String, Object> model) {
        return search(request, model);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String search(final HttpServletRequest request, Map<String, Object> model) {
        traceRequest(request);
        assignBaseVariables(request, model);
        return "search";
    }

    private void traceRequest(final HttpServletRequest request) {
        LOGGER.debug("URI {} requested.", getRequestUrl(request));
    }

    private String getRequestUrl(final HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();

        return requestURL.toString();
    }

    private void assignBaseVariables(final HttpServletRequest request, final Map<String, Object> model) {
        model.put("baseUrl", getBaseUrl(request));
    }

    private String getBaseUrl(final HttpServletRequest request) {
        final String port;

        if (request.getServerPort() == 80)  {
            port = "";
        } else {
            port = ":" + String.valueOf(request.getServerPort());
        }

        return new StringBuilder()
            .append(request.getScheme())
            .append("://")
            .append(request.getServerName())
            .append(port)
            .append(request.getServletPath())
            .append(request.getContextPath())
            .toString();
    }
}
