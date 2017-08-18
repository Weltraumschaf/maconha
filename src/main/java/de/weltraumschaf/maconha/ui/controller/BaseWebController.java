package de.weltraumschaf.maconha.ui.controller;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.MaconhaConfiguration;
import de.weltraumschaf.maconha.backend.repo.MediaFileRepo;
import de.weltraumschaf.maconha.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Common shared functionality web controllers.
 */
abstract class BaseWebController {
    private final RequestTracer tracer = new RequestTracer();
    private MediaFileRepo files;
    private MaconhaConfiguration config;


    @Autowired
    public final void setFiles(final MediaFileRepo files) {
        this.files = Validate.notNull(files, "files");
    }

    @Autowired
    public final void setConfig(final MaconhaConfiguration config) {
        this.config = Validate.notNull(config, "config");
    }

    final MaconhaConfiguration getConfig() {
        return config;
    }

    final void onMethodEntry(final HttpServletRequest request, final UriComponentsBuilder uri, final Map<String, Object> model) {
        tracer.traceRequest(request);
        assignBaseVariables(uri, model);
    }

    private void assignBaseVariables(final UriComponentsBuilder uri, final Map<String, Object> model) {
        model.put("baseUrl", uri.toUriString());
        model.put("numberOfIndexedFiles", files.count());
        model.put("version", config.getVersion());
    }
}
