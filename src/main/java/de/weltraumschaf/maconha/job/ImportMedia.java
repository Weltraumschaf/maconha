package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.core.FileExtension;
import de.weltraumschaf.maconha.core.FileNameExtractor;
import de.weltraumschaf.maconha.dao.MediaDao;
import de.weltraumschaf.maconha.dao.OriginFileDao;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.model.Media.MediaType;
import de.weltraumschaf.maconha.model.OriginFile;
import java.util.Collection;
import java.util.stream.Collectors;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * THis job import scanned media files.
 */
public final class ImportMedia extends BaseJob<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportMedia.class);
    private final FileNameExtractor extractor = new FileNameExtractor();
    @Autowired
    private OriginFileDao input;
    @Autowired
    private MediaDao output;
    private LocalDateTime importTime;

    public ImportMedia() {
        super(generateName(ImportMedia.class));
    }

    void setInput(final OriginFileDao input) {
        this.input = Validate.notNull(input, "input");
    }

    void setOutput(final MediaDao output) {
        this.output = Validate.notNull(output, "output");
    }

    LocalDateTime getImportTime() {
        return importTime;
    }

    @Override
    protected Void execute() throws Exception {
        final Collection<OriginFile> allScannedFiles = input.findAll();
        LOGGER.debug("Found {} scanned files.", allScannedFiles);
        final Collection<OriginFile> notImportedFiles = allScannedFiles.stream()
            .filter(file -> file.getImported() == null)
            .collect(Collectors.toList());
        LOGGER.debug("There are {} files not imported yet.", notImportedFiles);
        importTime = new LocalDateTime();
        notImportedFiles.stream().forEach(file -> importFile(file));
        return null;
    }

    private void importFile(final OriginFile file) {
        LOGGER.debug("Import file {}.", file);
        final FileExtension extension = extractor.extractExtension(file.getAbsolutePath());
        final Media imported = new Media()
            .setType(MediaType.VIDEO) // FIXME Make it dynamic.
            .setFormat(extension)
            .setTitle(extractor.extractTitle(file.getAbsolutePath()))
            .setLastImported(importTime)
            .setOriginFile(file);
        output.save(imported);
    }
}
