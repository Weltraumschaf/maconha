package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.shell.Command;
import de.weltraumschaf.maconha.shell.Commands;
import de.weltraumschaf.maconha.shell.Result;
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

    public HashFilesJob() {
        super(generateName(HashFilesJob.class));
    }

    @Override
    protected Description description() {
        return DESCRIPTION;
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

        final Command dirhash = new Commands(binValues.get(0)).dirhash(null);
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
