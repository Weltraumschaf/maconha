package de.weltraumschaf.maconha.job;

import java.util.Collections;
import java.util.EnumSet;
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
    private int seconds;

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
    public void setBaseDir(final int seconds) {
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
        throw new UnsupportedOperationException("Not supported yet.");
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
