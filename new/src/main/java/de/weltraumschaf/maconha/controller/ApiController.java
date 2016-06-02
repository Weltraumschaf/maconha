package de.weltraumschaf.maconha.controller;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.core.Validator;
import de.weltraumschaf.maconha.job.Job;
import de.weltraumschaf.maconha.job.JobConfig;
import de.weltraumschaf.maconha.job.JobDescription;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.service.JobService;
import de.weltraumschaf.maconha.service.SearchService;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * THis controller serves the REST API.
 */
@RestController
public final class ApiController {

    static final String BASE_URI_PATH = "/api";
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);
    private final Validator validator = new Validator();
    private final JobService jobs;
    private final SearchService search;

    @Autowired
    public ApiController(final JobService jobs, final SearchService search) {
        super();
        this.jobs = Validate.notNull(jobs, "jobs");
        this.search = Validate.notNull(search, "search");
    }

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
    public Collection<JobDescription> listrunningJobs() {
        LOGGER.debug("Serve API job list.");
        return jobs.list();
    }

    @RequestMapping(
        value = BASE_URI_PATH + "/jobs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JobDescription> submit(final @RequestBody JobConfig config, final UriComponentsBuilder uri) {
        LOGGER.debug("Serve job submission for config {}.", config);

        final Job<?> job = jobs.create(config.getName());
        jobs.submit(job);

        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri.path(BASE_URI_PATH + "/jobs/{id}").buildAndExpand(job.describe().getName()).toUri());
        return new ResponseEntity<>(job.describe(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(
        value = BASE_URI_PATH + "/search",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection<Media> search(final @RequestParam(value = "q") String query) {
        return search.search(validator.cleanSearchQuery(query));
    }
}