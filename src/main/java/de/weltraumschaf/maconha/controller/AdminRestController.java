package de.weltraumschaf.maconha.controller;

import de.weltraumschaf.maconha.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/admin/api")
public final class AdminRestController {

    /**
     * Service which will do all data retrieval/manipulation work
     */
    @Autowired
    private AdminService admin;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
