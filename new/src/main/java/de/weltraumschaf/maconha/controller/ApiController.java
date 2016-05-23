package de.weltraumschaf.maconha.controller;

import java.util.Arrays;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 */
@RestController
public final class ApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);
    private static final String BASE_URI_PATH = "/api";

    @RequestMapping(
        value = BASE_URI_PATH,
        method = RequestMethod.GET,
        produces = "text/uri-list")
    public String index(final UriComponentsBuilder uri) {
        LOGGER.debug("Serve API index.");
        return new StringBuilder()
            .append("# Available API for ").append(uri.path(BASE_URI_PATH).toUriString()).append("\r\n")
            .append(uri.path(BASE_URI_PATH + "/jobs").toUriString()).append("\r\n")
            .toString();
    }

    @RequestMapping(
        value = BASE_URI_PATH + "/jobs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection<String> listrunningJobs() {
        LOGGER.debug("Serve API job list.");
        return Arrays.asList("foo", "bar", "baz");
    }

    @RequestMapping(
        value = BASE_URI_PATH + "/jobs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> submitNewJob() {
        LOGGER.debug("Serve API job submit.");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
