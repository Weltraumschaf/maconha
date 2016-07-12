package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.service.MediaService;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This job import scanned media files.
 */
@JobImplementation
final class ImportMediaJob extends BaseJob<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportMediaJob.class);

    static final Description DESCRIPTION = new Description(ImportMediaJob.class);
    @Autowired
    private MediaService service;

    ImportMediaJob() {
        super(generateName(ImportMediaJob.class));
    }

    void setService(final MediaService service) {
        this.service = Validate.notNull(service, "service");
    }

    LocalDateTime getImportTime() {
        return service.getStartTime();
    }

    @Override
    protected Description description() {
        return DESCRIPTION;
    }

    @Override
    protected Void execute() throws Exception {
        LOGGER.debug("execute job {}.", getClass().getSimpleName());
        service.importMedia(monitor());
        return null;
    }

}
