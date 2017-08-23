package de.weltraumschaf.maconha.backend.service.scan.eventloop;


import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.service.scan.reporting.Reporter;

import java.util.Map;

/**
 * The context of an event which will be passed to an {@link EventHandler handler} alongside with the event to process.
 * <p>
 * This type is immutable.
 * </p>
 */
public final class EventContext {

    private final EventEmitter emitter;
    private final Map<Global, Object> globals;
    private final Reporter reporter;

    /**
     * Dedicated constructor.
     *
     * @param emitter  must not be {@code null}
     * @param globals  must not be {@code null}
     * @param reporter must not be {@code null}
     */
    public EventContext(final EventEmitter emitter, final Map<Global, Object> globals, final Reporter reporter) {
        super();
        this.emitter = Validate.notNull(emitter, "emitter");
        this.globals = Validate.notNull(globals, "globals");
        this.reporter = Validate.notNull(reporter, "reporter");
    }

    /**
     * Get the event emitter.
     * <p>
     * Use this to emit events from inside an {@link EventHandler event handler}.
     * </p>
     *
     * @return never {@code null}
     */
    public EventEmitter emitter() {
        return emitter;
    }

    /**
     * Get the store for global values.
     * <p>
     * Use this to pass global values to another {@link EventHandler event handler}.
     * </p>
     *
     * @return never {@code null}
     */
    public Map<Global, Object> globals() {
        return globals;
    }

    /**
     * Get the reporter for the current running loop.
     *
     * @return never {@code null}
     */
    public Reporter reporter() {
        return reporter;
    }

    @Override
    public String toString() {
        return "EventContext{" +
            "emitter=" + emitter +
            ", globals=" + globals +
            ", reporter=" + reporter +
            '}';
    }
}
