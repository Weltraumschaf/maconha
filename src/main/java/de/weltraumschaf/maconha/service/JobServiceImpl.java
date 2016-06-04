package de.weltraumschaf.maconha.service;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.job.JobDescription;
import de.weltraumschaf.maconha.job.GenerateIndex;
import de.weltraumschaf.maconha.job.Job;
import de.weltraumschaf.maconha.job.JobExecutor;
import de.weltraumschaf.maconha.job.ScanDirectory;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 */
@Service
final class JobServiceImpl implements JobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

    private final JobExecutor jobs;

    @Autowired
    public JobServiceImpl(final JobExecutor jobs) {
        super();
        this.jobs = Validate.notNull(jobs, "jobs");
    }

    @Override
    public Job<?> create(final String name) {
        if ("GenerateIndex".equalsIgnoreCase(name)) {
            return new GenerateIndex();
        } else if ("ScanDirectory".equalsIgnoreCase(name)) {
            return new ScanDirectory();
        } else {
            throw new IllegalArgumentException(String.format("Unknown job name '%s'!", name));
        }
    }

    @Override
    public void submit(final Job<?> job) {
        jobs.submit(job);
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
    public Collection<JobDescription> list() {
        return jobs.list();
    }

    @Override
    public Collection<JobDescription> list(Class<? extends Job> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}