package de.weltraumschaf.maconha.ui.controller;

import de.weltraumschaf.maconha.app.MaconhaConfiguration;
import de.weltraumschaf.maconha.backend.repo.MediaFileRepo;
import de.weltraumschaf.maconha.backend.service.UserService;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Common shared functionality.
 */
abstract class BaseWebController {
    private final RequestTracer tracer = new RequestTracer();
    private final MediaFileRepo files;
    final MaconhaConfiguration config;
    final UserService users;

    BaseWebController(final MediaFileRepo files, final MaconhaConfiguration config, final UserService users) {
        super();
        this.files = files;
        this.config = config;
        this.users = users;
    }

    void onMethodEntry(final HttpServletRequest request, final UriComponentsBuilder uri, final Map<String, Object> model) {
        tracer.traceRequest(request);
        assignBaseVariables(uri, model);
    }

    private void assignBaseVariables(final UriComponentsBuilder uri, final Map<String, Object> model) {
        model.put("baseUrl", uri.toUriString());
        model.put("numberOfIndexedFiles", files.count());
        model.put("version", config.getVersion());
    }
}
