package de.weltraumschaf.maconha.service;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.job.JobInfo;
import de.weltraumschaf.maconha.job.Job;
import de.weltraumschaf.maconha.job.JobExecutor;
import de.weltraumschaf.maconha.job.Jobs;
import java.util.Collection;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 */
@Service
final class DefaultJobService implements JobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJobService.class);
    private final JobExecutor executor;
    private final Jobs jobs;

    @Autowired
    public DefaultJobService(final JobExecutor executor, final Jobs jobs) {
        super();
        this.executor = Validate.notNull(executor, "executor");
        this.jobs = Validate.notNull(jobs, "jobs");
    }

    @Override
    public Job<?> create(final String name, final Map<String, Object> config) {
        LOGGER.debug("Create new job for name '{}' with config '{}'.", name, config);
        final Job job = jobs.create(name);
        job.configure(config);
        return job;
    }

    @Override
    public void submit(final Job<?> job) {
        LOGGER.debug("Job submission: {}.", job);
        executor.submit(job);
    }

    @Override
    public void cancel(final String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cancel(Class<? extends Job> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<JobInfo> list() {
        return executor.list();
    }

    @Override
    public Collection<JobInfo> list(Class<? extends Job> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
