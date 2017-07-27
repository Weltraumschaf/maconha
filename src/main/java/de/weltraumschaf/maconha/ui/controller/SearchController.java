package de.weltraumschaf.maconha.ui.controller;

import de.weltraumschaf.maconha.core.NotAlphaNumeric;
import de.weltraumschaf.maconha.backend.model.entity.MediaFile;
import de.weltraumschaf.maconha.backend.model.MediaType;
import de.weltraumschaf.maconha.backend.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
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
        return search.forKeywords(sanitizeQuery(query), sanitizeTypes(Arrays.asList(types)));
    }


    Collection<String> sanitizeQuery(final String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(query.trim().split("\\s+"))
            .filter(new NotAlphaNumeric().negate())
            .collect(Collectors.toList());
    }

    Collection<MediaType> sanitizeTypes(final Collection<String> types) {
        if (types == null || types.isEmpty()) {
            return EnumSet.allOf(MediaType.class);
        }

        for (final String type : types) {
            if ("all".equalsIgnoreCase(type.trim())) {
                return EnumSet.allOf(MediaType.class);
            }
        }

        return types.stream()
            .map(String::trim)
            .map(MediaType::forValue)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/files/**", method = RequestMethod.GET)
    public HttpEntity<PathResource> download(final HttpServletRequest request) throws IOException {
        tracer.traceRequest(request);
        final String strippedFilename = request.getRequestURI().replace("/files/", "");
        final String decodedFilename = URLDecoder.decode(strippedFilename, StandardCharsets.UTF_8.name());

        final SearchService.Download download = search.downloadFile(decodedFilename);
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, download.getFormat());
        final String basename = download.getFile().getFileName().toString();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncoder.encode(basename, StandardCharsets.UTF_8.name()));
        headers.setContentLength(download.getFile().toFile().length());

        return new HttpEntity<>(new PathResource(download.getFile()), headers);
    }

}
