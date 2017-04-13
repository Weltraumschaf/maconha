package de.weltraumschaf.maconha;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main entry point of the Spring boot application.
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan({"de.weltraumschaf.maconha"})
@PropertySource(value = {"classpath:application.properties"})
public class MaconhaApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaconhaApplication.class);

    @Autowired
    private Environment environment;

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
    }

}
