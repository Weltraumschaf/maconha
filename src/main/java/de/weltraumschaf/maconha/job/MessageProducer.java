package de.weltraumschaf.maconha.job;

/**
 * Implementors can produce messages.
 */
public interface MessageProducer {

    /**
     * Registers a consumer to receive messages.
     *
     * @param consumer must not be {@code null}
     */
    void register(MessageConsumer consumer);

    /**
     * Emits a message to all registered consumers.
     * <p>
     * The given message string is interpreted as {@link String#format(java.lang.String, java.lang.Object...)
     * format string}, so the optional arguments are interpolated into the message.
     * </p>
     *
     * @param message must not be {@code null} or empty
     * @param args optional format string arguments
     */
    void emit(String message, Object ... args);

}
