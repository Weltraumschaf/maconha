package de.weltraumschaf.maconha.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Configures the web application.
 * <p>
 * Must not be final for framework sake.
 * </p>
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "de.weltraumschaf.maconha")
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MvcConfiguration.class);
    private static final String VIEWS_PREFIX = "/WEB-INF/views/";
    private static final String VIEWS_SUFFIX = ".jsp";
    private static final String RESOURCES_PATTERN = "/static/**";
    private static final String RESOURCES_LOCATION = "/static/";

    @Override
    public void configureViewResolvers(final ViewResolverRegistry registry) {
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix(VIEWS_PREFIX);
        viewResolver.setSuffix(VIEWS_SUFFIX);
        LOGGER.debug("Register view resolver with prefix '{}' and suffix '{}'.", VIEWS_PREFIX, VIEWS_SUFFIX);
        registry.viewResolver(viewResolver);
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        LOGGER.debug("Register resource handler for pattern '{}' to location '{}'.", RESOURCES_PATTERN, RESOURCES_LOCATION);
        registry.addResourceHandler(RESOURCES_PATTERN).addResourceLocations(RESOURCES_LOCATION);
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }

}
