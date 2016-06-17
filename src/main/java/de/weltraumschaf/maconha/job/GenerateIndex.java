package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.service.MediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This job indexes imported media.
 */
final class GenerateIndex extends BaseJob<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateIndex.class);
    static final Description DESCRIPTION = new Description(GenerateIndex.class);
    @Autowired
    private MediaService service;

    GenerateIndex() {
        super(generateName(GenerateIndex.class));
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
