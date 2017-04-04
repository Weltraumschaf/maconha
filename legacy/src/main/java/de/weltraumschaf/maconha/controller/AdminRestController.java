package de.weltraumschaf.maconha.controller;

import de.weltraumschaf.maconha.job.JobDescription;
import de.weltraumschaf.maconha.job.Job;
import de.weltraumschaf.maconha.job.JobConfig;
import de.weltraumschaf.maconha.service.AdminService;

import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 */
@RestController
//@RequestMapping("/admin/api")
@RequestMapping(value = "/test", consumes = "application/json", produces = "application/json")
public final class AdminRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminRestController.class);

    private final AdminService admin;

    private final JobService jobs;

    @Autowired
    public AdminRestController(final AdminService admin, final JobService jobs) {
        super();
        this.admin = admin;
        this.jobs = jobs;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        LOGGER.debug("Serve admin index.");
        return "index\n";
    }

    @RequestMapping(value = "/jobs/", method = RequestMethod.GET)
    public Collection<JobDescription> jobs() {
        LOGGER.debug("Serve admin job list.");
        return jobs.list();
    }

    @RequestMapping(value = "/jobs/", method = RequestMethod.POST)
    public ResponseEntity<JobDescription> submit(final @RequestBody JobConfig config, final UriComponentsBuilder uri) {
        String name  ="ScanDirectory";

        LOGGER.debug("Serve job submission for {}.", name);
        final Job<?> job = jobs.create(name);
        jobs.submit(job);

        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri.path("/user/{id}").buildAndExpand(job.describe().getName()).toUri());
        return new ResponseEntity<>(job.describe(), headers, HttpStatus.CREATED);
    }
}
