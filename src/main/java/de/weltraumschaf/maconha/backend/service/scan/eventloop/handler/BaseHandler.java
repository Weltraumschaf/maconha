package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.Event;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventContext;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventHandler;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.IllegalEventData;

/**
 * Shared functionality for {@link EventHandler}.
 */
abstract class BaseHandler implements EventHandler {
    private final StopWatch watch = new StopWatch();
    private final String description;

    /**
     * Dedicated constructor.
     *
     * @param description must not be {@code null} or empty
     */
    protected BaseHandler(final String description) {
        super();
        this.description = Validate.notEmpty(description, "description");
    }

    final void assertPreConditions(final EventContext context, final Event event, final Class<?> expectedDataType) {
        Validate.notNull(context, "context");
        Validate.notNull(event, "event");

        if (event.getData() == null) {
            throw new IllegalEventData("The passed in event has no data (is null)!");
        }

        if (!expectedDataType.isAssignableFrom(event.getData().getClass())) {
            throw new IllegalEventData(
                "The passed in event data is not of type %s!",
                expectedDataType.getName());
        }
    }

    @Override
    public final void process(final EventContext context, final Event event) {
        watch.start();
        doWork(context, event);
        watch.stop();
        context.reporter().normal(getClass(), "Event handler for %s finished in %d ms.", description, watch.duration());
    }

    abstract void doWork(final EventContext context, final Event event);

}
