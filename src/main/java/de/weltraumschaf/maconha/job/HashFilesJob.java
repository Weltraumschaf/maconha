package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.shell.Command;
import de.weltraumschaf.maconha.shell.Commands;
import de.weltraumschaf.maconha.shell.Result;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;

/**
 * Hashes all files in given directory by external program.
 */
final class HashFilesJob extends BaseJob<Void> {

    static final Description DESCRIPTION = new Description(
        HashFilesJob.class,
        EnumSet.allOf(RquiredProperty.class),
        Collections.emptySet()
    );
    private static final Logger LOGGER = LoggerFactory.getLogger(HashFilesJob.class);

    @Autowired
    private ApplicationContext appContext;
    private Path baseDir;

    public HashFilesJob() {
        super(generateName(HashFilesJob.class));
    }

    @Override
    protected Description description() {
        return DESCRIPTION;
    }

    /**
     * Set the base dir to hash.
     * <p>
     * This must be set before execution, unless it will throw exceptions.
     * </p>
     *
     * @param baseDir must not be {@code null}
     */
    public void setBaseDir(final String baseDir) {
        this.baseDir = Paths.get(Validate.notNull(baseDir, "baseDir"));
    }

    @Override
    protected Void execute() throws Exception {
        final ApplicationArguments arguments = appContext.getBean("springApplicationArguments", ApplicationArguments.class);
        final List<String> binValues = arguments.getOptionValues("bin");

        if (binValues.size() != 1) {
            throw new JobExecutionError(
                String.format(
                    "Required command line argument 'bin' in application context has not size of one argument (was: %d!",
                    binValues.size()));
        }

        final Command dirhash = new Commands(Paths.get(binValues.get(0))).dirhash(null);
        LOGGER.debug("Calling external program: {}.", dirhash);
        final Result result = dirhash.execute();
        return null;
    }

    private enum RquiredProperty implements Property {
        DIR("dir");
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
