package de.weltraumschaf.maconha.backend.service.scan.eventloop;

/**
 *
 */
public class EventLoopError extends RuntimeException {
    EventLoopError(final String message, final Throwable cause, final Object ... arguments) {
        super(String.format(message, arguments), cause);
    }
}
