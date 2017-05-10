package de.weltraumschaf.maconha.frontend.search.controller;

import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.core.ServedContentTypes;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Handles installation stuff.
 */
@Controller
public final class InstallController extends BaseWebController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstallController.class);

    @Lazy
    @Autowired
    public InstallController(final MediaFileRepo files, final MaconhaConfiguration config, final UserService users) {
        super(files, config, users);
    }

    @GetMapping(value = "/install", produces = ServedContentTypes.TEXT_HTML)
    public ModelAndView install(final HttpServletRequest request, final UriComponentsBuilder uri, final Map<String, Object> model) {
        onMethodEntry(request, uri, model);

        if (users.isThereNoAdminUser()) {
            model.put("installationDone", false);
            return new ModelAndView("install", model);
        }

        return new ModelAndView("redirect:/");
    }

    @PostMapping(value = "/install", produces = ServedContentTypes.TEXT_HTML)
    public ModelAndView createAdmin(final HttpServletRequest request, final UriComponentsBuilder uri, final Map<String, Object> model) {
        onMethodEntry(request, uri, model);

        if (users.isThereNoAdminUser()) {
            final String username = request.getParameter("username");
            final String password = request.getParameter("password");
            LOGGER.debug("Create first admin with name {}.", username);
            users.createAdmin(username, password);
            model.put("installationDone", true);
            return new ModelAndView("install", model);
        }

        return new ModelAndView("redirect:/");
    }
}
