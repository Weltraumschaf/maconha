package de.weltraumschaf.maconha.backend.service.scan.eventloop;

/**
 * Thrown if an {@link Event} has the wrong type of {@link Event#getData() data}.
 */
public final class IllegalEventData extends RuntimeException {
    public IllegalEventData(final String message, final Object ... argumets) {
        super(String.format(message, argumets));
    }
}
