package de.weltraumschaf.maconha;

import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.service.ScanServiceFactory;
import de.weltraumschaf.maconha.service.scan.batch.ScanBatchConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Main entry point of the Spring boot application.
 */
@EnableAsync
@SpringBootApplication
@EnableBatchProcessing(modular = true) // Modular because job configuration is in other class.
@ComponentScan( {"de.weltraumschaf.maconha"})
@EnableConfigurationProperties(MaconhaConfiguration.class)
@PropertySource(value = {"classpath:application.properties"})
public class MaconhaApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaconhaApplication.class);

    private final Environment environment;

    private final MaconhaConfiguration config;

    @Autowired
    public MaconhaApplication(final Environment environment, final MaconhaConfiguration config) {
        super();
        this.environment = environment;
        this.config = config;
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
        LOGGER.info("Maconha version: {}", config.getVersion());
        LOGGER.info("Used profiles: {}", Arrays.toString(environment.getActiveProfiles()));

        if (config.isDebug()) {
            LOGGER.warn("Debugging is enabled by environment variable MACONHA_DEBUG! Should not be enabled in production.");
        }
    }

    @Bean
    public ApplicationContextFactory scanJobFactory() {
        // Provide a context factory for the scan job so the job registry will be populated with the job.
        return new GenericApplicationContextFactory(ScanBatchConfiguration.class);
    }

    @Bean
    public FactoryBean serviceLocatorFactoryBean() {
        final ServiceLocatorFactoryBean bean = new ServiceLocatorFactoryBean();
        bean.setServiceLocatorInterface(ScanServiceFactory.class);
        return bean;
    }

}
