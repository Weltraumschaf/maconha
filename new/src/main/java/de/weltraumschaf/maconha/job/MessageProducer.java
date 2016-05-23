package de.weltraumschaf.maconha.job;

/**
 */
public interface MessageProducer {

    void register(MessageConsumer consumer);

    void emmit(String message, Object ... args);

}
