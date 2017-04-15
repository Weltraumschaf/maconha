package de.weltraumschaf.maconha;

import de.weltraumschaf.maconha.service.scan.ScanBatchConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Main entry point of the Spring boot application.
 */
@SpringBootApplication
@EnableBatchProcessing(modular = true)
@ComponentScan( {"de.weltraumschaf.maconha"})
@PropertySource(value = {"classpath:application.properties"})
public class MaconhaApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaconhaApplication.class);

    private final Environment environment;

    @Autowired
    public MaconhaApplication(final Environment environment) {
        super();
        this.environment = environment;
    }

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

    @Override
    public void run(final String... args) throws Exception {
        // Here we can do additional things on startup.
    }

    @Bean
    public ApplicationContextFactory scanJobFactory() {
        return new GenericApplicationContextFactory(ScanBatchConfiguration.class);
    }
}
