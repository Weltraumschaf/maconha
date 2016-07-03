package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;

/**
 * Thrown if any error occurred during job creation.
 */
public final class JobGenerationError extends JobError {

    /**
     * Convenience constructor without cause.
     *
     * @param message must not be {@code null or empty}
     */
    public JobGenerationError(final String message) {
        this(message, null);
    }

    /**
     * Dedicated constructor.
     *
     * @param message must not be {@code null or empty}
     * @param cause may be {@code null}
     */
    public JobGenerationError(final String message, final Throwable cause) {
        super(Validate.notNull(message, "message"), cause);
    }

}
