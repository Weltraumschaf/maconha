package de.weltraumschaf.maconha.backend.service.scan.eventloop;

import de.weltraumschaf.commons.validate.Validate;

import java.util.Objects;

/**
 * Used to represent events processed by the event loop.
 * <p>
 * This type is immutable.
 * </p>
 */
public final class Event {
    /**
     * Type of the event.
     */
    private final EventType type;
    /**
     * Dat of the event.
     */
    private final Object data;

    /**
     * Dedicated constructor.
     *
     * @param type must not be {@code null}
     * @param data must not be {@code null}
     */
    public Event(final EventType type, final Object data) {
        super();
        this.type = Validate.notNull(type, "type");
        this.data = Validate.notNull(data, "data");
    }

    /**
     * Get the type of the event.
     *
     * @return never {@code null} or empty
     */
    public EventType getType() {
        return type;
    }

    /**
     * Get the payload.
     *
     * @return never {@code null}
     */
    public Object getData() {
        return data;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Event)) {
            return false;
        }

        final Event event = (Event) o;
        return Objects.equals(type, event.type) &&
            Objects.equals(data, event.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, data);
    }

    @Override
    public String toString() {
        return "Event{" +
            "type='" + type + '\'' +
            ", data=" + data +
            '}';
    }
}
