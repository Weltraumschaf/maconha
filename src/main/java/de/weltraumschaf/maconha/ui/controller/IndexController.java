package de.weltraumschaf.maconha.ui.controller;

import de.weltraumschaf.maconha.app.MaconhaConfiguration;
import de.weltraumschaf.maconha.backend.repo.MediaFileRepo;
import de.weltraumschaf.maconha.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Controller to serve the home site.
 */
@Controller
public final class IndexController extends BaseWebController {

    private final UserService users;

    @Autowired
    public IndexController(final MediaFileRepo files, final MaconhaConfiguration config, final UserService users) {
        super(files, config);
        this.users = users;
    }

    @GetMapping(value = "/", produces = ServedContentTypes.TEXT_HTML)
    public ModelAndView index(final HttpServletRequest request, final UriComponentsBuilder uri, final Map<String, Object> model) {
        onMethodEntry(request, uri, model);

        if (users.isThereNoAdminUser()) {
            return new ModelAndView("redirect:/install");
        }

        model.put("title", config.getTitle());

        return new ModelAndView("index", model);
    }

}
