package de.weltraumschaf.maconha.backend.service.scan.eventloop;

/**
 * Indicates that the processing of an {@link EventHandler event handler} failed.
 */
public final class EventHandlerFailed extends RuntimeException {
    public EventHandlerFailed(final String message, final Throwable cause, final Object ... arguments) {
        super(String.format(message, arguments), cause);
    }
}
