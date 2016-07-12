package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * Factory to create jobs.
 */
@Component
public final class Jobs {

    private static final Logger LOGGER = LoggerFactory.getLogger(Jobs.class);

    /**
     * Maps name of jobs to its description.
     */
    private final Map<String, Description> lookup;

    /**
     * Necessary to inject dependencies into created service.
     */
    private final AutowireCapableBeanFactory beanFactory;

    /**
     * Dedicated constructor.
     *
     * @param beanFactory must not be {@code null}
     */
    @Autowired
    public Jobs(final AutowireCapableBeanFactory beanFactory) {
        super();
        this.beanFactory = Validate.notNull(beanFactory, "beanFactory");
        lookup = init();
    }

    /**
     * Creates a not configured job by name.
     *
     * @param name must not be {@code null} or empty
     * @return never {@code null}, always new instance
     */
    public Job create(final String name) {
        Validate.notEmpty(name, "name");
        final Description description;

        if (lookup.containsKey(name)) {
            description = lookup.get(name);
        } else {
            throw new JobGenerationError(
                String.format("There is no description for a job with name '%s'", name));
        }

        final Job newJob;

        try {
            LOGGER.debug("Create job for name {}.", name);
            newJob = description.type().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new JobGenerationError(
                String.format("Can not instatiate job '%s' (cause '%s')!",
                    description.type().getSimpleName(), ex.getMessage()), ex);
        }

        beanFactory.autowireBean(newJob);
        return newJob;
    }

    Set<Class<?>> findImplementations() {
        final Reflections reflections = new Reflections(getClass().getPackage().getName());
        return reflections.getTypesAnnotatedWith(JobImplementation.class);
    }

    Map<String, Description> generateLookUp(final Set<Class<?>> found) {
        return found.stream()
            .filter(type -> {
                return BaseJob.class.isAssignableFrom(type);
            })
            .map(type -> {
                try {
                    return ((BaseJob)type.newInstance()).description();
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new JobGenerationError(ex.getMessage(), ex);
                }
            }).collect(Collectors.toMap(Description::name, Function.identity()));
    }

    private Map<String, Description> init() {
        return generateLookUp(findImplementations());
    }
}
