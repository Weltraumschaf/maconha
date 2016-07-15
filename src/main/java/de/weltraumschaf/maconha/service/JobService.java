package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.job.JobInfo;
import de.weltraumschaf.maconha.job.Job;
import java.util.Collection;
import java.util.Map;

/**
 */
public interface JobService {

    Job<?> create(String name, Map<String, Object> config);

    void submit(Job<?> job);

    void cancel(String name);

    void cancel(Class<? extends Job> type);

    Collection<JobInfo> list();

    Collection<JobInfo> list(Class<? extends Job> type);

    void pushChangesToWebSocket() throws InterruptedException;
}
