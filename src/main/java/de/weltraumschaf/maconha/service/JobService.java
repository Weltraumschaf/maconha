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

    Collection<JobInfo> list();

    void pushChangesToWebSocket();
}
