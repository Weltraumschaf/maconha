package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.service.MediaService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.EnumSet;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This job scans a directory for media files.
 */
@JobImplementation
final class ScanDirectoryJob extends BaseJob<Void> {

    static final Description DESCRIPTION = new Description(
        ScanDirectoryJob.class,
        EnumSet.allOf(RquiredProperty.class),
        Collections.emptySet());
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanDirectoryJob.class);
    private Path baseDir;

    @Autowired
    private MediaService service;

    /**
     * Dedicated constructor.
     */
    public ScanDirectoryJob() {
        super(generateName(ScanDirectoryJob.class));
    }

    /**
     * Injection point for service.
     *
     * @param service must not be {@code null}
     */
    void setService(final MediaService service) {
        this.service = Validate.notNull(service, "service");
    }

    /**
     * Set the base directory to scan.
     * <p>
     * This must be set before execution, unless it will throw exceptions.
     * </p>
     *
     * @param baseDir must not be {@code null}
     */
    public void setBaseDir(final String baseDir) {
        this.baseDir = Paths.get(Validate.notNull(baseDir, "baseDir"));
    }

    /**
     * Get the time of scan.
     *
     * @return never{@code null}
     */
    LocalDateTime getScanTime() {
        return service.getStartTime();
    }

    @Override
    protected Description description() {
        return DESCRIPTION;
    }

    @Override
    protected Void execute() throws Exception {
        LOGGER.debug("execute job {}.", getClass().getSimpleName());
        validateBaseDir();
        service.scanDirecotry(monitor(), baseDir);
        return null;
    }

    private void validateBaseDir() throws IllegalStateException {
        if (null == baseDir) {
            throw new IllegalStateException("Base dir is not set! Call #setBaseDir(Path) first.");
        }

        if (!Files.exists(baseDir)) {
            throw new IllegalStateException(String.format("Base dir '%s' does not exist!", baseDir));
        }

        if (!Files.isReadable(baseDir)) {
            throw new IllegalStateException(String.format("Base dir '%s' is not readable!", baseDir));
        }

        if (!Files.isDirectory(baseDir)) {
            throw new IllegalStateException(String.format("Base dir '%s' is not a directory!", baseDir));
        }
    }

    private enum RquiredProperty implements Property {
        BASE_DIR("baseDir");
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
