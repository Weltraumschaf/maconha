package de.weltraumschaf.maconha.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Top level controller.
 */
@Controller
public final class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(final HttpServletRequest request, Map<String, Object> model, final UriComponentsBuilder uri) {
        return search(request, model, uri);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String search(final HttpServletRequest request, final Map<String, Object> model, final UriComponentsBuilder uri) {
        traceRequest(request);
        assignBaseVariables(uri, model);
        return "search";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String admin(final HttpServletRequest request, final Map<String, Object> model, final UriComponentsBuilder uri) {
        traceRequest(request);
        assignBaseVariables(uri, model);
        return "admin";
    }

    private void traceRequest(final HttpServletRequest request) {
        LOGGER.debug("URI {} requested.", getRequestUrl(request));
    }

    private String getRequestUrl(final HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();

        return requestURL.toString();
    }

    private void assignBaseVariables(final UriComponentsBuilder uri, final Map<String, Object> model) {
        model.put("baseUrl", uri.toUriString());
        model.put("apiUrl", uri.path(ApiController.BASE_URI_PATH).toUriString());
    }

}
