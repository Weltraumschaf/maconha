package de.weltraumschaf.maconha.frontend.search.controller;

import de.weltraumschaf.maconha.core.NotAlphaNumeric;
import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Controller to serve the search queries.
 */
@RestController
public final class SearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    private final RequestTracer tracer = new RequestTracer();
    private final SearchService search;

    @Autowired
    public SearchController(final SearchService search) {
        super();
        this.search = search;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<MediaFile> index(final HttpServletRequest request, final @RequestParam("q") String query) {
        tracer.traceRequest(request);
        LOGGER.debug("Search query: {}", query);
        return search.forKeywords(sanitizeQuery(query));
    }

    Collection<String> sanitizeQuery(final String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(query.trim().split("\\s+"))
            .filter(new NotAlphaNumeric().negate())
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/files/**", method = RequestMethod.GET)
    public void download(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        tracer.traceRequest(request);
        final String filename = request.getRequestURI().replace("/files/", "");
        search.downloadFile(filename, response);
    }

}
