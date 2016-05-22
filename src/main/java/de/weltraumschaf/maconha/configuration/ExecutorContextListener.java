package de.weltraumschaf.maconha.configuration;

import de.weltraumschaf.maconha.MaconhaRegistry;
import de.weltraumschaf.maconha.job.JobExecutor;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public final class ExecutorContextListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorContextListener.class);

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        MaconhaRegistry.INSTANCE.setJobExecutor(new JobExecutor());
        LOGGER.debug("Context initialized.");
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        MaconhaRegistry.INSTANCE.getJobExecutor().shutdown();
        LOGGER.debug("Context destroyed.");
    }

}
