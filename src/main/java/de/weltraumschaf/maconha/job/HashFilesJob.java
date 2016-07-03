package de.weltraumschaf.maconha.job;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;

/**
 */
final class HashFilesJob extends BaseJob<Void> {

    static final Description DESCRIPTION = new Description(HashFilesJob.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(HashFilesJob.class);
    private static final String EXTERNAL_PROGRAM = "dirhash";

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

        final String dirhash = binValues.get(0) + '/' + EXTERNAL_PROGRAM;
        LOGGER.debug("Calling external program: {}.", dirhash);
        return null;
    }

}
