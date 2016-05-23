package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.job.JobDescription;
import de.weltraumschaf.maconha.job.Job;
import java.util.Collection;

/**
 */
public interface JobService {

    Job<?> create(String name);
    void submit(Job<?> job);
    void cancel(String name);
    void cancel(Class<? extends Job> type);
    Collection<JobDescription> list();
    Collection<JobDescription> list(Class<? extends Job> type);
}
