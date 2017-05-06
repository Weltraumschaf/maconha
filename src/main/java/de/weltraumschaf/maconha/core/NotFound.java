package de.weltraumschaf.maconha.core;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Used to signal HTTP not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public final class NotFound extends RuntimeException {
    /**
     * Dedicated constructor.
     *
     * @param message must not be {@code null}, format string
     * @param args optional arguments for format string message
     */
    public NotFound(final String message, final Object... args) {
        super(String.format(message, args));
    }
}
