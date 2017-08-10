package de.weltraumschaf.maconha.backend.eventloop;

/**
 * Implementors emits events.
 */
public interface EventEmitter {
    /**
     * Emit a single event.
     *
     * @param event must not be {@code null}
     */
    void emmit(final Event event);
}
