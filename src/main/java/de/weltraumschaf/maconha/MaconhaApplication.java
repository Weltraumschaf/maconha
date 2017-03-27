package de.weltraumschaf.maconha;

import de.weltraumschaf.maconha.job.JobExecutor;
import de.weltraumschaf.maconha.service.JobService;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main entry point of the Spring boot application.
 */
@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan({"de.weltraumschaf.maconha"})
@PropertySource(value = {"classpath:application.properties"})
public class MaconhaApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaconhaApplication.class);
    private final JobExecutor executor = new JobExecutor();
    @Autowired
    private Environment environment;
    @Autowired
    private JobService jobs;

    /**
     * Invoked main method.
     *
     * @param args arguments from command line
     */
    public static void main(final String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(MaconhaApplication.class, args);
        LOGGER.trace("Provided beans:");

        final String[] beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames);

        for (final String beanName : beanNames) {
            LOGGER.trace(beanName);
        }
    }

    @PostConstruct
    public void postConstruct() {
        LOGGER.info("Used profiles: {}", Arrays.toString(environment.getActiveProfiles()));
    }

    @Bean
    public JobExecutor jobExecutor() {
        LOGGER.debug("Create new job executor.");
        return executor;
    }

    @Override
    public void run(final String... args) throws Exception {
        jobs.pushChangesToWebSocket();
    }

}
