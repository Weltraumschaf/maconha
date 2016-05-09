package de.weltraumschaf.maconha.job;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 */
abstract class BaseJob<V> implements Job<V>{

    final Collection<MessageConsumer> consumers = new CopyOnWriteArrayList<>();

    @Override
    public final void register(final MessageConsumer consumer) {
        consumers.add(consumer);
    }

    @Override
    public final void emmit(final String message) {
        consumers.stream().forEach((consumer) -> {
            consumer.receive(message);
        });
    }
}
