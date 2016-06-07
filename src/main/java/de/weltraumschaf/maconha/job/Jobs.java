package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * Factory to create jobs.
 */
@Component
public final class Jobs {

    /**
     * Maps name of jobs to its description.
     */
    private static final Map<String, Description> LOOKUP;

    static {
        final Map<String, Description> tmp = new HashMap<>();
        tmp.put(ScanDirectory.DESCRIPTION.name(), ScanDirectory.DESCRIPTION);
        tmp.put(ImportMedia.DESCRIPTION.name(), ImportMedia.DESCRIPTION);
        tmp.put(GenerateIndex.DESCRIPTION.name(), GenerateIndex.DESCRIPTION);
        LOOKUP = Collections.unmodifiableMap(tmp);
    }

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

        if (LOOKUP.containsKey(name)) {
            description = LOOKUP.get(name);
        } else {
            throw new JobGenerationError(
                String.format("There is no description for a job with name '%s'", name));
        }

        final Job newJob;

        try {
            newJob = description.type().newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new JobGenerationError(
                String.format("Can not instatiate job '%s' (cause '%s')!",
                    description.type().getSimpleName(), ex.getMessage()), ex);
        }

        beanFactory.autowireBean(newJob);
        return newJob;
    }
}
