package de.weltraumschaf.maconha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Main dispatching point for UI based clients.
 * <p>
 * Returns name of the servlet to use.
 * </p>
 */
@Controller
@RequestMapping("/")
public final class IndexController {

    @RequestMapping(method = RequestMethod.GET)
    public String searchPageAsIndex() {
        return "Search";
    }

    @RequestMapping(value="/admin", method = RequestMethod.GET)
    public String adminPage() {
        return "Admin";
    }

    @RequestMapping(value="/example", method = RequestMethod.GET)
    public String examplePage() {
        return "UserManagement";
    }

}
