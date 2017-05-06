package de.weltraumschaf.maconha.frontend.search.controller;

import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.UserService;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Common shared functionality.
 */
abstract class BaseWebController {
    private final RequestTracer tracer = new RequestTracer();
    private final MediaFileRepo files;
    final UserService users;

    BaseWebController(final MediaFileRepo files, final UserService users) {
        super();
        this.files = files;
        this.users = users;
    }

    void onMethodEntry(final HttpServletRequest request, final UriComponentsBuilder uri, final Map<String, Object> model) {
        tracer.traceRequest(request);
        assignBaseVariables(uri, model);
    }

    private void assignBaseVariables(final UriComponentsBuilder uri, final Map<String, Object> model) {
        model.put("baseUrl", uri.toUriString());
        model.put("numberOfIndexedFiles", files.count());
    }
}