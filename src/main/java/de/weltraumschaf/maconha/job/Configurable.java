package de.weltraumschaf.maconha.job;

import java.util.Map;

/**
 * Implementations can configure them self.
 */
public interface Configurable {

    /**
     * Configures the implementor with given map.
     *
     * @param config must not be {@code null}
     */
    void configure(final Map<String, Object> config);
}
