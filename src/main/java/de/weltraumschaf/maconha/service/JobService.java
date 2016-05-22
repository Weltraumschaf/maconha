package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.job.Description;
import de.weltraumschaf.maconha.job.Job;
import java.util.Collection;

/**
 */
public interface JobService {

    Job<?> create(String name);
    void submit(Job<?> job);
    void cancel(String name);
    void cancel(Class<? extends Job> type);
    Collection<Description> list();
    Collection<Description> list(Class<? extends Job> type);
}
