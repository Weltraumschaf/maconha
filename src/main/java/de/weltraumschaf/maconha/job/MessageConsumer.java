package de.weltraumschaf.maconha.job;

/**
 * Implementors can receive messages.
 */
public interface MessageConsumer {

    /**
     * Consumes a given message from named producer.
     *
     * @param message must not be {@code null}
     */
    void receive(final JobMessage message);
}
