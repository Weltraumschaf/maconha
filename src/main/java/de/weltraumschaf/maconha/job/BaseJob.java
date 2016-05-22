package de.weltraumschaf.maconha.job;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
abstract class BaseJob<V> implements Job<V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseJob.class);

    private final Collection<MessageConsumer> consumers = new CopyOnWriteArrayList<>();
    private final String name;
    private volatile State state = State.NEW;

    public BaseJob(final String name) {
        super();
        this.name = name;
    }

    @Override
    public final void register(final MessageConsumer consumer) {
        LOGGER.debug("Register message consumer {}.", consumer);
        consumers.add(consumer);
    }

    @Override
    public final void emmit(final String format, final Object ... args) {
        final String message = String.format(format, args);
        LOGGER.debug("Emmit message: {}", message);

        if (consumers.isEmpty()) {
            LOGGER.debug("No consumers registered.");
            return;
        }

        consumers.stream().forEach((consumer) -> {
            consumer.receive(message);
        });
    }

    @Override
    public final Description describe() {
        return new Description(name, getClass(), state);
    }

    @Override
    public final V call() throws Exception {
        LOGGER.debug("Job called {}.", describe());

        if (isRunning() || isRunning()) {
            throw new IllegalStateException(String.format("Can not call job in state '%s'!", state));
        }

        if (isCanceled()) {
            LOGGER.debug("Job is canceld ({}). Not excuting.", describe());
            return null;
        }

        state = State.RUNNING;
        final V result = execute();
        state = State.FINISHED;
        LOGGER.debug("Job finished ({}).", describe());
        return result;
    }

    @Override
    public final void cancel() {
        LOGGER.debug("Cancel job ({}).", describe());
        state = State.CANCELED;
    }

    @Override
    public final boolean isCanceled() {
        return state == State.CANCELED;
    }

    @Override
    public final boolean isRunning() {
        return state == State.RUNNING;
    }

    @Override
    public final boolean isFinished() {
        return state == State.FINISHED;
    }

    @Override
    public final State getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Job (" + describe().toString() + ')';
    }

    protected abstract V execute() throws Exception;

    protected static String generateName(final Class<? extends Job> task) {
        return task.getSimpleName() + UUID.randomUUID();
    }
}
