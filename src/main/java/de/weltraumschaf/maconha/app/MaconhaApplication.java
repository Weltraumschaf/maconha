package de.weltraumschaf.maconha.app;

import de.weltraumschaf.maconha.backend.model.entity.EntityBasePackage;
import de.weltraumschaf.maconha.backend.repo.RepoBasePackage;
import de.weltraumschaf.maconha.backend.service.MediaFileService;
import de.weltraumschaf.maconha.backend.service.ServiceBasePackage;
import de.weltraumschaf.maconha.ui.UiBasePackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.vaadin.spring.events.annotation.EnableEventBus;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Main entry point of the Spring boot application.
 */
@EnableAsync
@EnableEventBus
@EnableBatchProcessing(modular = true) // Modular because job configuration is in other class.
@EnableConfigurationProperties(MaconhaConfiguration.class)
@PropertySource(value = {"classpath:application.properties"})
@EnableJpaRepositories(basePackageClasses = {RepoBasePackage.class})
@EntityScan(basePackageClasses = {EntityBasePackage.class})
@SpringBootApplication(scanBasePackageClasses = {MaconhaApplication.class, UiBasePackage.class, ServiceBasePackage.class})
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
        SpringApplication.run(MaconhaApplication.class, args);
    }

    @PostConstruct
    public void postConstruct() {
        LOGGER.info("Maconha version: {}", config.getVersion());
        LOGGER.info("Used profiles: {}", Arrays.toString(environment.getActiveProfiles()));

        if (config.isDebug()) {
            LOGGER.warn("Debugging is enabled by environment variable MACONHA_DEBUG! Should not be enabled in production.");
        }
    }

}