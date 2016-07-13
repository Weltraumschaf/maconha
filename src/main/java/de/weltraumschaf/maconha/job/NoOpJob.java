package de.weltraumschaf.maconha.job;

import java.util.Collections;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This job does nothing than run for a given time period (for testing).
 */
@JobImplementation
final class NoOpJob extends BaseJob<Void> {

    static final Description DESCRIPTION = new Description(
        NoOpJob.class,
        EnumSet.allOf(RquiredProperty.class),
        Collections.emptySet());
    private static final Logger LOGGER = LoggerFactory.getLogger(NoOpJob.class);
    private int seconds = 1;

    /**
     * Dedicated constructor.
     */
    public NoOpJob() {
        super(generateName(ScanDirectoryJob.class));
    }

    /**
     * Set the seconds to run.
     * <p>
     * This must be set before execution, unless it will throw exceptions.
     * </p>
     *
     * @param seconds must not be less than 1
     */
    public void setSeconds(final int seconds) {
        if (seconds < 1) {
            throw new IllegalArgumentException(String.format("Parameter seconds must not be less than 1! Given %d.", seconds));
        }

        this.seconds = seconds;
    }

    @Override
    protected Description description() {
        return DESCRIPTION;
    }

    @Override
    protected Void execute() throws Exception {
        monitor().begin(seconds);
        emit("Waiting for %d seconds...", seconds);

        for (int i = 1; i <= seconds; ++i) {
            TimeUnit.SECONDS.sleep(1);
            monitor().worked(1);
            emit("%d of %d secondes done.", i, seconds);
        }

        emit("Ready, waited %d seconds.", seconds);
        monitor().done();
        return null;
    }

    private enum RquiredProperty implements Property {
        SECONDS("seconds");
        private final String name;

        private RquiredProperty(String name) {
            this.name = name;
        }

        @Override
        public String getBeanName() {
            return name;
        }
    }
}
