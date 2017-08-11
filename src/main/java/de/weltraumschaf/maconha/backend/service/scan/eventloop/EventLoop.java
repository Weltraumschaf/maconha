package de.weltraumschaf.maconha.backend.service.scan.eventloop;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.HasLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The main class which loops until there are no more events or the loop is {@link #stop() stopped}.
 */
public final class EventLoop implements HasLogger {
    /**
     * Stores global data available to all events via its {@link EventContext context}.
     */
    private final Map<String, Object> globals = new ConcurrentHashMap<>();
    /**
     * Stores the event handlers by event type.
     */
    private final Map<String, EventHandler> handlers = new ConcurrentHashMap<>();
    /**
     * Class to hold events.
     */
    private final EventQueue events = new DefaultQueue();
    /**
     * Indicates if the loop should be executed or not.
     */
    private volatile boolean running = true;

    /**
     * Start the vent loop with an initial event.
     *
     * @param initial may be {@code null}
     */
    public void start(final Event initial) {
        logger().debug("Start loop.");
        events.emmit(initial);

        while (running) {
            final Event e = events.next();
            process(e);

            if (events.isEmpty()) {
                logger().debug("No more events.");
                stop();
            }
        }
    }

    /**
     * Stops the loop.
     * <p>
     * A already processed event will be processed until it is finished.
     * </p>
     */
    public void stop() {
        logger().debug("Stopping loop.");
        running = false;
    }

    /**
     * Registers an {@link EventHandler event handler} for a particular event type.
     * <p>
     * There can only one handler be registered for a particular type.
     * </p>
     *
     * @param type    must not be {@code null} or empty
     * @param handler must not be {@code null}
     */
    public void register(final String type, final EventHandler handler) {
        Validate.notEmpty(type, "type");
        Validate.notNull(handler, "handler");

        if (handlers.containsKey(type)) {
            throw new IllegalArgumentException(String.format("There is already a handler registered for event type '%s'!", type));
        }

        handlers.put(type, handler);
    }

    private void process(final Event current) {
        if (null == current) {
            logger().debug("Current event is null, ignoring!");
            return;
        }

        final EventHandler handler = determineHandler(current);
        logger().debug("Processing event %s with handler %s", current, handler);
        handler.process(new EventContext(events, globals), current);
    }

    private EventHandler determineHandler(final Event e) {
        if (handlers.containsKey(e.getType())) {
            return handlers.get(e.getType());
        }

        return EventHandler.NONE;
    }

}
