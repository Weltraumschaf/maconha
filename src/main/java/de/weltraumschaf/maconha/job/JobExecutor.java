package de.weltraumschaf.maconha.job;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 */
@Component
public final class JobExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutor.class);
    private static final int TIMEOUT = 60;
    private final ExecutorService pool = Executors.newFixedThreadPool(10);
    private final Collection<Job<?>> jobs = new CopyOnWriteArrayList<>();
    private volatile boolean shutdown;

    @PreDestroy
    public void shutdown() {
        LOGGER.debug("Shutdown job executor...");

        if (shutdown) {
            LOGGER.debug("Already shut down, ignoring.");
            return;
        }

        shutdown = true;
        pool.shutdown(); // Disable new tasks from being submitted
        cancelJobs();

        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks

                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
                    LOGGER.error("Thread pool did not terminate.");
                }
            }
        } catch (final InterruptedException ex) {
            LOGGER.warn("Got exception while shutdown pool.", ex);
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    public void submit(final Job<?> job) {
        LOGGER.debug("Submit new job: {}.", job);

        if (shutdown) {
            LOGGER.debug("Aleady shut down, ignoring job {}.", job);
            return;
        }

        jobs.add(job);
        pool.submit(job);
    }

    public Collection<JobInfo> list() {
        return jobs.stream().map(task -> task.info()).collect(Collectors.toList());
    }

    private void cancelJobs() {
        jobs.stream().filter(j -> j.isRunning()).forEach(j -> j.cancel());
    }
}
