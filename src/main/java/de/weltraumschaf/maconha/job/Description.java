package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import java.util.Collections;
import java.util.Set;

/**
 * Describes a job.
 */
final class Description {

    /**
     * Type of job.
     */
    private final Class<? extends Job> type;
    /**
     * Required configuration properties.
     */
    private final Set<Property> required;
    /**
     * Optional configuration properties.
     */
    private final Set<Property> optional;

    /**
     * Convenience constructor for no configuration properties.
     *
     * @param type must not be {@code null}
     */
    Description(final Class<? extends Job> type) {
        this(type, Collections.emptySet(), Collections.emptySet());
    }

    /**
     * Dedicated constructor.
     *
     * @param type must not be {@code null}
     * @param required must not be {@code null}
     * @param optional must not be {@code null}
     */
    Description(final Class<? extends Job> type, Set<? extends Property> required, Set<? extends Property> optional) {
        super();
        this.type = Validate.notNull(type, "type");
        this.required = Collections.unmodifiableSet(Validate.notNull(required, "required"));
        this.optional = Collections.unmodifiableSet(Validate.notNull(optional, "optional"));
    }

    /**
     * Get the type to create instances from.
     *
     * @return never {@code null}
     */
    Class<? extends Job> type() {
        return type;
    }

    /**
     * Get the name of the job.
     *
     * @return never {@code null} or empty
     */
    String name() {
        return type().getSimpleName();
    }

    /**
     * Get the required configuration properties.
     *
     * @return never {@code null}, maybe empty, unmodfiable
     */
    Set<Property> required() {
        return required;
    }

    /**
     * Get the optional configuration properties.
     *
     * @return never {@code null}, maybe empty, unmodfiable
     */
    Set<Property> optional() {
        return optional;
    }
}
