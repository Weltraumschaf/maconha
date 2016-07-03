package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.service.MediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This job indexes imported media.
 */
final class GenerateIndexJob extends BaseJob<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateIndexJob.class);
    static final Description DESCRIPTION = new Description(GenerateIndexJob.class);
    @Autowired
    private MediaService service;

    GenerateIndexJob() {
        super(generateName(GenerateIndexJob.class));
    }

    void setService(final MediaService service) {
        this.service = Validate.notNull(service, "service");
    }


    @Override
    public Void execute() throws Exception {
        LOGGER.debug("execute job {}.", getClass().getSimpleName());
        service.generateIndex(monitor());
        return null;
    }

    @Override
    protected Description description() {
        return DESCRIPTION;
    }

}
