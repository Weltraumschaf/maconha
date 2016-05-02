package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.configuration.ExecutorContextListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public final class JobExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorContextListener.class);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void shutdown() {
        LOGGER.debug("Shutdown executor...");
        final List<Runnable> unfinished = executor.shutdownNow();
    }

    public void submit(final Job task) {
        executor.submit(task);
    }
}
