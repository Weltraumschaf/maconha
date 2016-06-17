package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.job.ProgressMonitor;
import java.io.IOException;
import java.nio.file.Path;
import org.joda.time.LocalDateTime;

/**
 */
public interface MediaService {

    LocalDateTime getStartTime();
    void scanDirecotry(final ProgressMonitor monitor, final Path baseDir) throws IOException;
    void importMedia(final ProgressMonitor monitor);
    void generateIndex(final ProgressMonitor monitor);
}
