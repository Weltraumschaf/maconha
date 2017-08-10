package de.weltraumschaf.maconha.backend.eventloop;

/**
 * Implementors process a single event.
 */
public interface EventHandler {
    /**
     * Used instead of {@code null}.
     */
    EventHandler NONE = (ctx, e) -> {
        System.out.println("Event handler NONE called. Doing nothing.");
    };

    /**
     * Process a single event.
     *
     * @param context must not be {@code null}
     * @param event must not be {@code null}
     */
    void process(EventContext context, Event event);
}
