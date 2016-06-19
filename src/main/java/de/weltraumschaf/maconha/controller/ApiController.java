package de.weltraumschaf.maconha.controller;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.core.Validator;
import de.weltraumschaf.maconha.job.Job;
import de.weltraumschaf.maconha.job.JobInfo;
import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.model.OriginFile;
import de.weltraumschaf.maconha.service.JobService;
import de.weltraumschaf.maconha.service.MediaService;
import de.weltraumschaf.maconha.service.SearchService;
import java.util.Collection;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
 * This controller serves the REST API.
 */
@RestController
public final class ApiController {

    static final String BASE_URI_PATH = "/api";
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);
    private final Validator validator = new Validator();
    private final JobService jobs;
    private final SearchService search;
    private final MediaService medias;

    @Autowired
    public ApiController(final JobService jobs, final SearchService search, final MediaService medias) {
        super();
        this.jobs = Validate.notNull(jobs, "jobs");
        this.search = Validate.notNull(search, "search");
        this.medias = Validate.notNull(medias, "medias");
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
    public Collection<JobInfo> listrunningJobs() {
        LOGGER.debug("Serve API job list.");
        return jobs.list();
    }

    @RequestMapping(
        value = BASE_URI_PATH + "/jobs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JobInfo> submit(
        final @RequestParam("name") String name,
        final @RequestBody Map<String, Object> config,
        final UriComponentsBuilder uri) {
        LOGGER.debug("Serve job submission for {} with config {}.", name, config);

        final Job<?> job = jobs.create(name, config);
        jobs.submit(job);

        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri.path(BASE_URI_PATH + "/jobs/{id}").buildAndExpand(job.info().getName()).toUri());
        return new ResponseEntity<>(job.info(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(
        value = BASE_URI_PATH + "/search",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection<Media> search(final @RequestParam(value = "q") String query) {
        return search.search(validator.cleanSearchQuery(query));
    }

    @RequestMapping(
        value = BASE_URI_PATH + "/file",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection<OriginFile> allFiles(
        final @RequestParam(value = "page", required = false) Integer page,
        final @RequestParam(value = "size", required = false) Integer size)
    {
        return medias.allFiles(new PageRequest(validatePage(page), validateSize(size)));
    }

    @RequestMapping(
        value = BASE_URI_PATH + "/media",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection<Media> allMedias(
        final @RequestParam(value = "page", required = false) Integer page,
        final @RequestParam(value = "size", required = false) Integer size)
    {
        return medias.allMedias(new PageRequest(validatePage(page), validateSize(size)));
    }

    @RequestMapping(
        value = BASE_URI_PATH + "/keyword",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Collection<Keyword> allKeywords(
        final @RequestParam(value = "page", required = false) Integer page,
        final @RequestParam(value = "size", required = false) Integer size)
    {
        return medias.allKeywords(new PageRequest(validatePage(page), validateSize(size)));
    }

    private int validatePage(final Integer input) {
        if (null == input || input < 0) {
            return 0;
        }

        return input;
    }

    private int validateSize(final Integer input) {
        if (null == input || input < 1) {
            return 10;
        }

        return input;
    }
}
