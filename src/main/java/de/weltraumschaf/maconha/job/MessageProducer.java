package de.weltraumschaf.maconha.job;

/**
 */
public interface MessageProducer {

    void register(final MessageConsumer consumer);

    void emmit(final String message);

    default void emmit(final String format, final Object ... args) {
        emmit(String.format(format, args));
    }

}
