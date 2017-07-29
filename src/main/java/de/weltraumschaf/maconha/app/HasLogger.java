package de.weltraumschaf.maconha.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HasLogger is a feature interface that provides Logging capability for anyone
 * implementing it where logger needs to operate in serializable environment
 * without being static.
 */
public interface HasLogger {

    /**
     * Creates a logger based on class name.
     *
     * @return never {@code null}
     */
    default Logger logger() {
        return LoggerFactory.getLogger(getClass());
    }
}
