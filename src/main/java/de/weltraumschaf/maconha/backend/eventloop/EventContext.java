package de.weltraumschaf.maconha.backend.eventloop;


import de.weltraumschaf.commons.validate.Validate;

import java.util.Map;

/**
 * The context of an event which will be passed to an {@link EventHandler handler} alongside with the event to process.
 * <p>
 * This type is immutable.
 * </p>
 */
public final class EventContext {

    private final EventEmitter emitter;
    private final Map<String, Object> globals;

    /**
     * Dedicated constructor.
     *
     * @param emitter must not be {@code null}
     * @param globals must not be {@code null}
     */
    EventContext(final EventEmitter emitter, final Map<String, Object> globals) {
        super();
        this.emitter = Validate.notNull(emitter, "emitter");
        this.globals = Validate.notNull(globals, "globals");
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
     * @return must not be {@code null}
     */
    public Map<String, Object> globals() {
        return globals;
    }

    @Override
    public String toString() {
        return "EventContext{" +
            "emitter=" + emitter +
            ", globals=" + globals +
            '}';
    }
}
