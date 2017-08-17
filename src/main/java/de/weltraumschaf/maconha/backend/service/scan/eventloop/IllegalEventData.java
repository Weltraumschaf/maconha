package de.weltraumschaf.maconha.backend.service.scan.eventloop;

/**
 * Thrown if an {@link Event} has the wrong type of {@link Event#getData() data}.
 */
public final class IllegalEventData extends EventLoopError {
    public IllegalEventData(final String message, final Object... argumets) {
        super(message, null, argumets);
    }
}
