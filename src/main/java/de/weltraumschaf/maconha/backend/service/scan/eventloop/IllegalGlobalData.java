package de.weltraumschaf.maconha.backend.service.scan.eventloop;

/**
 * Thrown if the global store has the wrong type of data.
 */
public final class IllegalGlobalData extends EventLoopError {
    public IllegalGlobalData(final String message, final Object... argumets) {
        super(message, null, argumets);
    }
}
