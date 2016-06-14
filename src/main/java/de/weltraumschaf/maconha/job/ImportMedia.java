package de.weltraumschaf.maconha.job;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.core.FileExtension;
import de.weltraumschaf.maconha.core.FileNameExtractor;
import de.weltraumschaf.maconha.model.Media;
import de.weltraumschaf.maconha.model.Media.MediaType;
import de.weltraumschaf.maconha.model.OriginFile;
import de.weltraumschaf.maconha.repos.MediaRepo;
import de.weltraumschaf.maconha.repos.OriginFileRepo;
import java.util.Collection;
import java.util.stream.Collectors;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This job import scanned media files.
 */
final class ImportMedia extends BaseJob<Void> {

    static final Description DESCRIPTION = new Description(ImportMedia.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportMedia.class);
    private final FileNameExtractor extractor = new FileNameExtractor();
    @Autowired
    private OriginFileRepo input;
    @Autowired
    private MediaRepo output;
    private LocalDateTime importTime;

    ImportMedia() {
        super(generateName(ImportMedia.class));
    }

    void setInput(final OriginFileRepo input) {
        this.input = Validate.notNull(input, "input");
    }

    void setOutput(final MediaRepo output) {
        this.output = Validate.notNull(output, "output");
    }

    LocalDateTime getImportTime() {
        return importTime;
    }

    @Override
    protected Description description() {
        return DESCRIPTION;
    }

    @Override
    protected Void execute() throws Exception {
        final Collection<OriginFile> allScannedFiles = input.findAll();
        begin(allScannedFiles.size());
        LOGGER.debug("Found {} scanned files.", allScannedFiles.size());
        final Collection<OriginFile> notImportedFiles = allScannedFiles.stream()
            .filter(file -> file.getImported() == null)
            .collect(Collectors.toList());
        LOGGER.debug("There are {} files not imported yet.", notImportedFiles.size());
        importTime = new LocalDateTime();
        worked(allScannedFiles.size() - notImportedFiles.size());
        notImportedFiles.stream().forEach(file -> importFile(file));
        return null;
    }

    private void importFile(final OriginFile file) {
        LOGGER.debug("Import file {}.", file.getAbsolutePath());
        final FileExtension extension = extractor.extractExtension(file.getAbsolutePath());
        final Media imported = new Media()
            .setType(MediaType.forValue(extension))
            .setFormat(extension)
            .setTitle(extractor.extractTitle(file.getAbsolutePath()))
            .setLastImported(importTime)
            .setOriginFile(file);
        output.save(imported);
        worked(1);
    }
}
