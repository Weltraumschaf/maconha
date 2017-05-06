package de.weltraumschaf.maconha.frontend.search.controller;

import de.weltraumschaf.maconha.core.ServedContentTypes;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    private final RequestTracer tracer = new RequestTracer();
    private final MediaFileRepo files;
    private final UserService users;

    @Lazy
    @Autowired
    public IndexController(final MediaFileRepo files, final UserService users) {
        super();
        this.files = files;
        this.users = users;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = ServedContentTypes.TEXT_HTML)
    public String index(final HttpServletRequest request, final UriComponentsBuilder uri, final Map<String, Object> model) {
        onMethodEntry(request, uri, model);
        return "index";
    }

    @RequestMapping(value = "/install", method = RequestMethod.GET, produces = ServedContentTypes.TEXT_HTML)
    public String install(final HttpServletRequest request, final UriComponentsBuilder uri, final Map<String, Object> model) {
        onMethodEntry(request, uri, model);

        if (users.isThereNoAdminUser()) {
            model.put("createAdminUser", true);
        } else {
            model.put("createAdminUser", false);
        }

        return "install";
    }

    @RequestMapping(value = "/install", method = RequestMethod.POST, produces = ServedContentTypes.TEXT_HTML)
    public String createAdmin(final HttpServletRequest request, final UriComponentsBuilder uri, final Map<String, Object> model) {
        onMethodEntry(request, uri, model);
        model.put("createAdminUser", false);

        if (users.isThereNoAdminUser()) {
            final String username = request.getParameter("username");
            final String password = request.getParameter("password");
            LOGGER.debug("Create first admin with name {}.", username);
            users.createAdmin(username, password);
        } else {
            LOGGER.warn("Ignore admin user creation because there is already an admin user account.");
        }

        return "install";
    }

    private void onMethodEntry(final HttpServletRequest request, final UriComponentsBuilder uri, final Map<String, Object> model) {
        tracer.traceRequest(request);
        assignBaseVariables(uri, model);
    }

    private void assignBaseVariables(final UriComponentsBuilder uri, final Map<String, Object> model) {
        model.put("baseUrl", uri.toUriString());
        model.put("numberOfIndexedFiles", files.count());
    }
}
