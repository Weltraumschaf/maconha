package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;

/**
 * Thrown if any error occurred during {@link Configurable#configure(java.util.Map)}.
 */
public final class JobConfigurationError extends Error {

    /**
     * Convenience constructor without cause.
     *
     * @param message must not be {@code null or empty}
     */
    public JobConfigurationError(final String message) {
        this(message, null);
    }

    /**
     * Dedicated constructor.
     *
     * @param message must not be {@code null or empty}
     * @param cause may be {@code null}
     */
    public JobConfigurationError(final String message, final Throwable cause) {
        super(Validate.notEmpty(message, "message"), cause);
    }

}
