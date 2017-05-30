package de.weltraumschaf.maconha.service.scan;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.model.*;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import de.weltraumschaf.maconha.service.MediaFileService;
import de.weltraumschaf.maconha.service.ScanService;
import de.weltraumschaf.maconha.service.ScanServiceFactory;
import de.weltraumschaf.maconha.model.FileMetaData;
import de.weltraumschaf.maconha.service.scan.extraction.KeywordsFromFileNameExtractor;
import de.weltraumschaf.maconha.service.scan.extraction.KeywordsFromMetaDataExtractor;
import de.weltraumschaf.maconha.service.scan.extraction.MetaDataExtractor;
import de.weltraumschaf.maconha.service.scan.hashing.HashFileReader;
import de.weltraumschaf.maconha.service.scan.hashing.HashedFile;
import de.weltraumschaf.maconha.shell.Commands;
import de.weltraumschaf.maconha.shell.Result;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Thread executor based implementation.
 * <p>
 * This class is package private because it must not be used directly from outside. Use the DI of Spring to get an
 * instance.
 * </p>
 */
@Service(ScanServiceFactory.THREAD)
final class ThreadScanService extends BaseScanService implements ScanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadScanService.class);

    private final MaconhaConfiguration config;
    @Deprecated // TODO Move into MediaFileService.
    private final MediaFileRepo mediaFileRepo;
    private final KeywordRepo keywords;
    private final MediaFileService mediaFiles;
    private Commands cmds;

    @Lazy
    @Autowired
    ThreadScanService(final MaconhaConfiguration config, final MediaFileRepo mediaFileRepo, final KeywordRepo keywords, final MediaFileService mediaFiles) {
        super();
        this.config = config;
        this.mediaFileRepo = mediaFileRepo;
        this.keywords = keywords;
        this.mediaFiles = mediaFiles;
    }

    @PostConstruct
    public void init() {
        LOGGER.debug("Initialize thread based scan service.");
        cmds = new Commands(Paths.get(config.getBindir()));
    }

    @Async
    @Override
    @Transactional
    public void scan(final Bucket bucket, final UI currentUi) {
        long id = 0L;
        Notification notification = notification(
            "Scan job started",
            "Scan for bucket '%s' in directory '%s' with id %d started.",
            bucket.getName(), bucket.getDirectory(), id);
        notifyClient(id, notification, currentUi);
        final DateTime startTime = DateTime.now();

        try {
            scanImpl(id, bucket, currentUi);
        } catch (final IOException | InterruptedException e) {
            LOGGER.warn(e.getMessage(), e);
            notification = notification(
                "Scan job failed",
                "Scan for bucket '%s' in directory '%s' failed with error: %s",
                bucket.getName(), bucket.getDirectory(), e.getMessage());
            notifyClient(id, notification, currentUi);
            return;
        }

        final DateTime endTime = DateTime.now();
        final String duration = secondsFormat.print(new Duration(startTime, endTime).toPeriod());
        notification = notification(
            "Scan job finished",
            "Scan for bucket '%s' in directory '%s' with id %d finished in %s.",
            bucket.getName(), bucket.getDirectory(), id, duration);
        notifyClient(id, notification, currentUi);
    }

    private void scanImpl(final long id, final Bucket bucket, final UI currentUi) throws IOException, InterruptedException {
        final Result result = cmds.dirhash(Paths.get(bucket.getDirectory())).execute();

        if (result.isFailed()) {
            LOGGER.warn("Scan job with id {} failed with exit code {} and STDERR: {}", id, result.getExitCode(), result.getStderr());
            final Notification notification = notification(
                "Scan job failed",
                "Scan for bucket '%s' in directory '%s' failed with error: %s",
                bucket.getName(), bucket.getDirectory(), result.getStderr());
            notifyClient(id, notification, currentUi);
            return;
        }

        LOGGER.debug(result.getStdout());
        new HashFileReader().read(Paths.get(bucket.getDirectory()).resolve(".checksums")).stream()
            .map(hashedFile -> hashedFile.relativizeFilename(bucket))
            .filter(hashedFile -> mediaFiles.isFileUnseen(hashedFile, bucket))
            .forEach(hashedFile -> extractMetaData(bucket, hashedFile));
    }

    private void extractMetaData(final Bucket bucket, final HashedFile file) {
        // TODO Remove duplicated code.
        LOGGER.debug("Extract meta data for: {}", file.getFile());
        final FileExtension extension;

        try {
            extension = file.extractExtension();
        } catch (final IllegalArgumentException e) {
            LOGGER.warn(e.getMessage());
            LOGGER.warn("Skipping file {}", file.getFile());
            return;
        }

        final FileMetaData fileMetaData = mediaFiles.extractFileMetaData(bucket, file);

        final MediaFile media = new MediaFile();
        media.setType(MediaType.forValue(extension));
        media.setFormat(fileMetaData.getMime());
        media.setRelativeFileName(file.getFile());
        media.setFileHash(file.getHash());
        media.setBucket(bucket);

        final Collection<String> foundKeywords = new HashSet<>();
        foundKeywords.addAll(new KeywordsFromFileNameExtractor().extract(file.getFile()));
        foundKeywords.addAll(new KeywordsFromMetaDataExtractor().extract(fileMetaData.getData()));

        foundKeywords.stream()
            .filter(new MalformedKeywords())
            .filter(new IgnoredKeywords())
            .map(literal -> {
                Keyword keyword = keywords.findByLiteral(literal);

                if (null == keyword) {
                    LOGGER.debug("Save new keyword '{}'.", literal);
                    keyword = new Keyword();
                    keyword.setLiteral(literal);
                    keywords.save(keyword);
                }

                return keyword;
            }).forEach(media::addKeyword);

        mediaFileRepo.save(media);
    }

    @Override
    public boolean stop(final long executionId) {
        return false;
    }

    @Override
    public List<ScanStatus> overview() {
        return Collections.emptyList();
    }
}
