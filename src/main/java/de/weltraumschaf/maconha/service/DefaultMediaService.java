package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.core.FileExtension;
import de.weltraumschaf.maconha.core.FileFinder;
import de.weltraumschaf.maconha.core.FileNameExtractor;
import de.weltraumschaf.maconha.core.IgnoredKeywords;
import de.weltraumschaf.maconha.core.MalformedKeywords;
import de.weltraumschaf.maconha.job.ProgressMonitor;
import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.model.OriginFile;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.repo.MediaRepo;
import de.weltraumschaf.maconha.repo.OriginFileRepo;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.EnumSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation.
 */
@Service
@Transactional
final class DefaultMediaService implements MediaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMediaService.class);
    private final FileNameExtractor extractor = new FileNameExtractor();
    private final KeywordRepo keywordRepo;
    private final MediaRepo mediaRepo;
    private final OriginFileRepo originFileRepo;
    private LocalDateTime startTime = new LocalDateTime();

    @Autowired
    public DefaultMediaService(final KeywordRepo keywordRepo, final MediaRepo mediaRepo, final OriginFileRepo originFileRepo) {
        super();
        this.keywordRepo = keywordRepo;
        this.mediaRepo = mediaRepo;
        this.originFileRepo = originFileRepo;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public void scanDirecotry(final ProgressMonitor monitor, final Path baseDir) throws IOException {
        startTime = new LocalDateTime();
        LOGGER.debug("Scan dir {} at {} ...", baseDir, startTime);
        final Collection<Path> foundFiles = FileFinder.find(
            baseDir,
            EnumSet.complementOf(EnumSet.of(FileExtension.NONE)));
        monitor.begin(foundFiles.size());
        foundFiles
            .stream()
            .forEach(mediaFile -> scanFile(baseDir, mediaFile, startTime, monitor));
    }

    private void scanFile(final Path baseDir, final Path mediaFile, final LocalDateTime scanTime, final ProgressMonitor monitor) {
        LOGGER.debug("Scan file {} ...", mediaFile);
        final OriginFile file = new OriginFile();
        file.setBaseDir(baseDir);
        file.setAbsolutePath(mediaFile);
        file.setFingerprint(fingerprint(mediaFile));
        file.setScanTime(scanTime);
        originFileRepo.save(file);
        monitor.worked(1);
    }

    private String fingerprint(final Path mediaFile) {
        try {
            // TODO Read from prepared file.
            return DigestUtils.sha256Hex(Files.newInputStream(mediaFile));
        } catch (final IOException ex) {
            throw new IOError(ex);
        }
    }

    @Override
    public void importMedia(final ProgressMonitor monitor) {
        startTime = new LocalDateTime();
        final Collection<OriginFile> allScannedFiles = originFileRepo.findAll();
        monitor.begin(allScannedFiles.size());
        LOGGER.debug("Found {} scanned files.", allScannedFiles.size());
        final Collection<OriginFile> notImportedFiles = allScannedFiles.stream()
            .filter(file -> file.getImported() == null)
            .collect(Collectors.toList());
        LOGGER.debug("There are {} files not imported yet.", notImportedFiles.size());
        monitor.worked(allScannedFiles.size() - notImportedFiles.size());
        notImportedFiles.stream().forEach(file -> importFile(file, monitor, startTime));
    }

    private void importFile(final OriginFile file, final ProgressMonitor monitor, final LocalDateTime importTime) {
        LOGGER.debug("Import file {}.", file.getAbsolutePath());
        final FileExtension extension = extractor.extractExtension(file.getAbsolutePath());
        final Media imported = new Media()
            .setType(Media.MediaType.forValue(extension))
            .setFormat(extension)
            .setTitle(extractor.extractTitle(file.getAbsolutePath()))
            .setLastImported(importTime)
            .setOriginFile(file);
        mediaRepo.save(imported);
        monitor.worked(1);
    }

    @Override
    public void generateIndex(final ProgressMonitor monitor) {
        final Collection<Media> allImportedMedia = mediaRepo.findAll();
        monitor.begin(allImportedMedia.size());
        allImportedMedia.stream().forEach(media -> index(media, monitor));
    }

    private void index(final Media media, final ProgressMonitor monitor) {
        final OriginFile file = media.getOriginFile();

        if (null == file) {
            LOGGER.debug("Media has no origin file, ignoring ({}).", media);
            return;
        }

        // TODO Not only extract from file name, but also from title because will be editable.
        extract(file)
            .filter(new IgnoredKeywords())
            .filter(new MalformedKeywords())
            .forEach(literal -> saveIndex(literal, media));
        monitor.worked(1);
    }

    private Stream<String> extract(final OriginFile file) {
        return extractor.extractKeywords(file.getBaseDir(), file.getAbsolutePath()).stream();
    }

    private void saveIndex(final String literal, final Media media) {
        LOGGER.debug("Save keyword '{}' to media with id {}", literal, media.getId());
        Keyword keyword = keywordRepo.findByLiteral(literal);

        if (null == keyword) {
            LOGGER.debug("Create new keyword for literal '{}'.", literal);
            keyword = new Keyword().setLiteral(literal);
            keywordRepo.save(keyword);
        } else {
            LOGGER.debug("Use existing keyword with id {} for literal '{}'.", keyword.getId(), literal);

        }

        media.addKeyword(keyword);
        mediaRepo.save(media);
    }

    @Override
    public Collection<OriginFile> allFiles(final Pageable pageable) {
        return originFileRepo.findAll(pageable).getContent();
    }

    @Override
    public Collection<Media> allMedias(final Pageable pageable) {
        return mediaRepo.findAll(pageable).getContent();
    }

    @Override
    public Collection<Keyword> allKeywords(final Pageable pageable) {
        return keywordRepo.findAll(pageable).getContent();
    }
}
