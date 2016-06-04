package de.weltraumschaf.maconha;

import de.weltraumschaf.maconha.configuration.CorsFilter;
import de.weltraumschaf.maconha.configuration.ExecutorContextListener;
import de.weltraumschaf.maconha.job.JobExecutor;
import java.util.Arrays;
import java.util.EventListener;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Main entry point of the Spring boot application.
 */
@SpringBootApplication
public class MaconhaApplication implements WebApplicationInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaconhaApplication.class);
    private static final EventListener[] SERVLET_LISTENER = new EventListener[]{new ExecutorContextListener()};
    private static final String[] SERVLET_MAPPINGS = new String[]{
        "/maconha-ng/*"
    };

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

        for (String beanName : beanNames) {
            LOGGER.trace(beanName);
        }
    }

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        LOGGER.debug("On startup called.");

        for (final EventListener listener : SERVLET_LISTENER) {
            LOGGER.debug("Register context loader listeners {}.", listener.getClass().getName());
            sc.addListener(listener);
        }
    }

    @Bean
    public ServletContextInitializer initializer() {
        LOGGER.debug("Create servlet initializer.");

        return (final ServletContext servletContext) -> {
            LOGGER.debug("Servlet cotext initilaizer called.");

            for (final EventListener listener : SERVLET_LISTENER) {
                LOGGER.debug("Register context loader listeners {}.", listener.getClass().getName());
                servletContext.addListener(listener);
            }
        };
    }

    @Bean
    public ServletRegistrationBean dispatcherRegistration(final DispatcherServlet dispatcherServlet) {
        LOGGER.debug("Register URL mapping.");
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.addUrlMappings(SERVLET_MAPPINGS);
        return registration;
    }

    @Bean
    public Filter corsFilter() {
        LOGGER.debug("Register CORS filter.");
        return new CorsFilter();
    }

    @Bean
    public MessageSource messageSource() {
        LOGGER.debug("Load resource bundle for messages.");
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }

    @Bean
    public JobExecutor jobExecutor() {
        return MaconhaRegistry.INSTANCE.getJobExecutor();
    }
}
