package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;

/**
 */
public class JobError extends Error {

    public JobError(final String message, final Throwable cause) {
        super(Validate.notEmpty(message, "message"), cause);
    }

}
