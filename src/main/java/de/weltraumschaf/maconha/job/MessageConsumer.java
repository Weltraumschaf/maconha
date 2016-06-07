package de.weltraumschaf.maconha.job;

/**
 * Implementors can receive messages.
 */
public interface MessageConsumer {

    /**
     * Consumes a given message.
     *
     * @param message must not be {@code null} or empty
     */
    void receive(final String message);
}
