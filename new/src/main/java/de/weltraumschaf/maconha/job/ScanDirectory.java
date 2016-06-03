package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.core.FileFinder;
import de.weltraumschaf.maconha.core.Movies;
import de.weltraumschaf.maconha.dao.OriginFileDao;
import de.weltraumschaf.maconha.model.OriginFile;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This job scans a directory for media files.
 */
public final class ScanDirectory extends BaseJob<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanDirectory.class);
    @Autowired
    private OriginFileDao output;
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
     * This must be set before execution, unless it will thow exceptions.
     * </p>
     *
     * @param baseDir must not be {@code null}
     */
    public void setBaseDir(final Path baseDir) {
        this.baseDir = Validate.notNull(baseDir, "baseDir");
    }

    /**
     * Get the time of scan.
     *
     * @return never{@code null}
     */
    public LocalDateTime getScanTime() {
        return scanTime;
    }

    @Override
    public Void execute() throws Exception {
        validateBaseDir();
        scanTime = new LocalDateTime();
        LOGGER.debug("Scan dir {} at {} ...", baseDir, scanTime);
        FileFinder.find(baseDir, EnumSet.allOf(Movies.class)).stream().forEach(mediaFile -> scanFile(mediaFile));
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
    }

    private String fingerprint(final Path mediaFile) {
        try {
            return DigestUtils.sha256Hex(Files.newInputStream(mediaFile));
        } catch (final IOException ex) {
            throw new IOError(ex);
        }
    }
}
