package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.core.FileFinder;
import de.weltraumschaf.maconha.core.FileNameExtractor;
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
    private final FileNameExtractor extractor = new FileNameExtractor();
    @Autowired
    private OriginFileDao dao;
    private Path baseDir;

    public ScanDirectory() {
        super(generateName(ScanDirectory.class));
    }

    public void setBaseDir(final Path baseDir) {
        this.baseDir = Validate.notNull(baseDir, "baseDir");
    }

    @Override
    public Void execute() throws Exception {
        validateBaseDir();
        LOGGER.debug("Scan dir {} ...", baseDir);
        FileFinder.find(baseDir, EnumSet.allOf(Movies.class)).stream().forEach(mediaFile -> {
            try {
                scanFile(mediaFile);
            } catch (final IOException ex) {
                throw new IOError(ex);
            }
        });
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

    private void scanFile(final Path mediaFile) throws IOException {
        LOGGER.debug("Scan file {} ...", mediaFile);
        final OriginFile file = new OriginFile();
        file.setBaseDir(baseDir);
        file.setAbsolutePath(mediaFile);
        file.setFingerprint(DigestUtils.sha256Hex(Files.newInputStream(mediaFile)));
        file.setIndexTime(new LocalDateTime());
        dao.save(file);
    }
}
