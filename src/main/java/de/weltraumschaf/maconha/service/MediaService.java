package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.job.ProgressMonitor;
import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.model.OriginFile;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import org.springframework.data.domain.Pageable;

/**
 */
public interface MediaService {

    void scanDirecotry(final ProgressMonitor monitor, final Path baseDir) throws IOException;
    void importMedia(final ProgressMonitor monitor);
    void generateIndex(final ProgressMonitor monitor);

    Collection<OriginFile> allFiles(Pageable pageable);

    Collection<Media> allMedias(Pageable pageable);

    Collection<Keyword> allKeywords(Pageable pageable);
}
