package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.Event;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.EventContext;
import de.weltraumschaf.maconha.backend.service.scan.eventloop.IllegalEventData;

/**
 *
 */
abstract class BaseHandler {

    void assertPreConditions(final EventContext context, final Event event, final Class<?> expectedDataType) {
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
}
