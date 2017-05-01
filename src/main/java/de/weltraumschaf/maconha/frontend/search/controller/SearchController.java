package de.weltraumschaf.maconha.frontend.search.controller;

import de.weltraumschaf.maconha.core.NotAlphaNumeric;
import de.weltraumschaf.maconha.core.ServedContentTypes;
import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.model.MediaType;
import de.weltraumschaf.maconha.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = ServedContentTypes.APPLICATION_JSON)
    public Collection<MediaFile> index(
        final HttpServletRequest request,
        final @RequestParam("q") String query,
        final @RequestParam(name = "type[]", defaultValue = "") String[] types) {
        tracer.traceRequest(request);
        LOGGER.debug("Search q={} type={}", query, types);
        return search.forKeywords(sanitizeQuery(query), sanitizeTypes(types));
    }


    Collection<String> sanitizeQuery(final String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(query.trim().split("\\s+"))
            .filter(new NotAlphaNumeric().negate())
            .collect(Collectors.toList());
    }

    private Collection<MediaType> sanitizeTypes(final String[] types) {
        return Arrays.stream(types)
            .map(MediaType::forValue)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/files/**", method = RequestMethod.GET)
    public void download(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        tracer.traceRequest(request);
        final String strippedFilename = request.getRequestURI().replace("/files/", "");
        final String decodedFilename = URLDecoder.decode(strippedFilename, StandardCharsets.UTF_8.name());
        search.downloadFile(decodedFilename, response);
    }

}
