package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.core.FileExtension;
import de.weltraumschaf.maconha.core.FileFinder;
import de.weltraumschaf.maconha.model.OriginFile;
import de.weltraumschaf.maconha.repos.OriginFileRepo;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This job scans a directory for media files.
 */
final class ScanDirectory extends BaseJob<Void> {

    static final Description DESCRIPTION = new Description(
        ScanDirectory.class,
        EnumSet.allOf(RquiredProperty.class),
        Collections.emptySet());
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanDirectory.class);
    @Autowired
    private OriginFileRepo output;
    private Path baseDir;
    private LocalDateTime scanTime = new LocalDateTime();

    /**
     * Dedicated constructor.
     */
    public ScanDirectory() {
        super(generateName(ScanDirectory.class));
    }

    /**
     * Set the base dir to scan.
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
        return scanTime;
    }

    @Override
    protected Description description() {
        return DESCRIPTION;
    }

    @Override
    protected Void execute() throws Exception {
        validateBaseDir();
        scanTime = new LocalDateTime();
        LOGGER.debug("Scan dir {} at {} ...", baseDir, scanTime);
        final Collection<Path> foundFiles = FileFinder.find(
            baseDir,
            EnumSet.complementOf(EnumSet.of(FileExtension.NONE)));
        begin(foundFiles.size());
        foundFiles
                .stream()
                .forEach(mediaFile -> scanFile(mediaFile));
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

    private void scanFile(final Path mediaFile) {
        LOGGER.debug("Scan file {} ...", mediaFile);
        final OriginFile file = new OriginFile();
        file.setBaseDir(baseDir);
        file.setAbsolutePath(mediaFile);
        file.setFingerprint(fingerprint(mediaFile));
        file.setScanTime(scanTime);
        output.save(file);
        worked(1);
    }

    private String fingerprint(final Path mediaFile) {
        try {
            // TODO Read from prepared file.
            return DigestUtils.sha256Hex(Files.newInputStream(mediaFile));
        } catch (final IOException ex) {
            throw new IOError(ex);
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
