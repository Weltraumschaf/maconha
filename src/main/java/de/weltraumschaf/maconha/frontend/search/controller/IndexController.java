package de.weltraumschaf.maconha.frontend.search.controller;

import de.weltraumschaf.maconha.core.ServedContentTypes;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Value("${maconha.title}")
    private String title = "";

    @Lazy
    @Autowired
    public IndexController(final MediaFileRepo files, final UserService users) {
        super(files, users);
    }

    @GetMapping(value = "/", produces = ServedContentTypes.TEXT_HTML)
    public ModelAndView index(final HttpServletRequest request, final UriComponentsBuilder uri, final Map<String, Object> model) {
        onMethodEntry(request, uri, model);

        if (users.isThereNoAdminUser()) {
            return new ModelAndView("redirect:/install");
        }

        model.put("title", title);
        return new ModelAndView("index", model);
    }

}
