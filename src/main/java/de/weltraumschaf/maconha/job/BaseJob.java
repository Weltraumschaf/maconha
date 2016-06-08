package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * Base implementation of a job.
 */
abstract class BaseJob<V> implements Job<V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseJob.class);

    private final Collection<MessageConsumer> consumers = new CopyOnWriteArrayList<>();
    private final ProgressMonitor monitor = new ProgressMonitor();
    private final String name;
    private volatile State state = State.NEW;

    public BaseJob(final String name) {
        super();
        this.name = Validate.notEmpty(name, "name");
    }

    @Override
    public final void register(final MessageConsumer consumer) {
        Validate.notNull(consumer, "consumer");
        LOGGER.debug("Register message consumer {}.", consumer);
        consumers.add(consumer);
    }

    @Override
    public final void emit(final String format, final Object... args) {
        Validate.notNull(format, "format");
        final String message = String.format(format, args);
        LOGGER.debug("Job {} emmits message: {}", name, message);

        if (consumers.isEmpty()) {
            LOGGER.debug("Job {} has no consumers registered.", name);
            return;
        }

        consumers.stream().forEach((consumer) -> {
            consumer.receive(message);
        });
    }

    @Override
    public final JobInfo info() {
        return new JobInfo(name, state, monitor.progress());
    }

    @Override
    public final V call() throws Exception {
        LOGGER.debug("Job called {}.", info());

        if (isRunning() || isFinished()) {
            throw new IllegalStateException(String.format("Can not call job in state '%s'!", state));
        }

        if (isCanceled()) {
            LOGGER.debug("Job is canceld ({}). Not excuting.", info());
            return null;
        }

        state = State.RUNNING;
        final V result;

        try {
            result = execute();
        } catch (final Exception ex) {
            state = State.FAILED;
            LOGGER.error("Job failed due to exception in it's execute method!", ex);
            throw ex;
        }

        if (isCanceled()) {
            LOGGER.debug("Job was canceld ({}).", info());
            return null;
        }

        monitor.done();
        state = State.FINISHED;
        LOGGER.debug("Job finished ({}).", info());
        return result;
    }

    @Override
    public final void cancel() {
        LOGGER.debug("Cancel job ({}).", info());
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
    public final void configure(final Map<String, Object> config) {
        configure(Validate.notNull(config, "config"), description());
    }

    private void configure(final Map<String, Object> config, final Description description) {
        description.required().stream().forEach((property) -> {
            injectRequiredProperty(config, property);
        });
        description.optional().stream().forEach((property) -> {
            injectOptionalProperty(config, property);
        });
    }

    private void injectRequiredProperty(final Map<String, Object> config, final Property property) {
        injectProperty(config, property, true);
    }

    private void injectOptionalProperty(final Map<String, Object> config, final Property property) {
        injectProperty(config, property, false);
    }

    private void injectProperty(final Map<String, Object> config, final Property property, boolean isRequired) {
        if (!config.containsKey(property.getBeanName())) {
            if (isRequired) {
                throw new JobConfigurationError(
                    String.format("Missing required config property '%s' for job '%s'!",
                        property.getBeanName(), description().name()));
            } else {
                return;
            }
        }

        final PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(getClass(), property.getBeanName());

        if (null == descriptor) {
            if (isRequired) {
                throw new JobConfigurationError(
                    String.format("There is no bean property for '%s' in job '%s'",
                        property.getBeanName(), description().name()));
            } else {
                return;
            }
        }

        try {
            descriptor.getWriteMethod().invoke(this, config.get(property.getBeanName()));
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new JobConfigurationError(
                String.format("Can not set bean property '%s' on job '%s' (cause '%s')!",
                    property.getBeanName(), description().name(), ex.getMessage()), ex);
        }
    }

    protected final void begin(final int totalWork) {
        monitor.begin(totalWork);
    }

    protected final void worked(final int work) {
        monitor.worked(work);
    }

    @Override
    public String toString() {
        return "Job (" + info().toString() + ')';
    }

    protected abstract Description description();

    protected abstract V execute() throws Exception;

    protected static String generateName(final Class<? extends Job> task) {
        return task.getSimpleName() + "-" + UUID.randomUUID();
    }
}
