package de.weltraumschaf.maconha.frontend.search.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import de.weltraumschaf.maconha.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Controller to serve the home site.
 */
@Controller
public final class IndexController {

    private final RequestTracer tracer = new RequestTracer();

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String index(final HttpServletRequest request, final UriComponentsBuilder uri, final Map<String, Object> model) {
        tracer.traceRequest(request);
        assignBaseVariables(uri, model);
        return "index";
    }

    private void assignBaseVariables(final UriComponentsBuilder uri, final Map<String, Object> model) {
        model.put("baseUrl", uri.toUriString());
    }

}
