package de.weltraumschaf.maconha.frontend.search.controller;

import de.weltraumschaf.maconha.core.ServedContentTypes;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Controller to serve the home site.
 */
@Controller
public final class IndexController {

    private final RequestTracer tracer = new RequestTracer();
    private final MediaFileRepo files;

    @Lazy
    @Autowired
    public IndexController(final MediaFileRepo files) {
        super();
        this.files = files;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = ServedContentTypes.TEXT_HTML)
    public String index(final HttpServletRequest request, final UriComponentsBuilder uri, final Map<String, Object> model) {
        tracer.traceRequest(request);
        model.put("baseUrl", uri.toUriString());
        model.put("numberOfIndexedFiles", files.count());
        return "index";
    }

}
