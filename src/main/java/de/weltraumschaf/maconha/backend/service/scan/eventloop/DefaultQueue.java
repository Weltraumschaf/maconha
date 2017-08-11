package de.weltraumschaf.maconha.backend.service.scan.eventloop;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.HasLogger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Default implementation.
 */
final class DefaultQueue implements EventQueue, HasLogger {
    private final Queue<Event> events = new ConcurrentLinkedQueue<>();

    @Override
    public void emmit(final Event event) {
        Validate.notNull(event, "event");
        logger().debug("Emmit event of type '{}'.", event.getType());
        events.offer(event);
    }

    @Override
    public Event next() {
        return events.poll();
    }

    @Override
    public boolean isEmpty() {
        return events.isEmpty();
    }

    /**
     * Number of {@link Event events} in queue.
     *
     * @return not negative
     */
    int size() {
        return events.size();
    }
}
