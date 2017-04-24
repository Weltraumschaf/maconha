package de.weltraumschaf.maconha.frontend.search.controller;

import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

/**
 * Controller to serve the search queries.
 */
@RestController
public final class SearchController {

    private final RequestTracer tracer = new RequestTracer();
    private final SearchService search;

    @Autowired
    public SearchController(final SearchService search) {
        super();
        this.search = search;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<MediaFile> index(final @RequestParam("q") String query) {
        return Collections.emptyList();
    }
}
