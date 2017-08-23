package de.weltraumschaf.maconha.backend.service.scan.eventloop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementors process a single event.
 */
public interface EventHandler {
    /**
     * Used instead of {@code null}.
     */
    EventHandler NONE = new EventHandler() {
        private final Logger logger = LoggerFactory.getLogger(EventHandler.class);

        @Override
        public void process(final EventContext context, final Event event) {
            logger.debug("Event handler NONE called. Doing nothing.");
        }
    } ;

    /**
     * Process a single event.
     *
     * @param context must not be {@code null}
     * @param event must not be {@code null}
     */
    void process(EventContext context, Event event);

}
