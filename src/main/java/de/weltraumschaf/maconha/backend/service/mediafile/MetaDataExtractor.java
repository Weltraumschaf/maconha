package de.weltraumschaf.maconha.backend.service.mediafile;

import de.weltraumschaf.maconha.backend.model.FileMetaData;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Extracts {@link FileMetaData meta data} from the file content.
 */
public final class MetaDataExtractor implements Extractor<FileMetaData> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetaDataExtractor.class);

    private final Tika tika = new Tika();

    @Override
    public FileMetaData extract(final String path) {
        final Metadata metadata = new Metadata();
        final Path file = Paths.get(path);

        try (final TikaInputStream input = TikaInputStream.get(file)) {
            final MediaType mimetype = tika.getDetector().detect(input, metadata);
            return new FileMetaData(mimetype.toString(), tika.parseToString(input));
        } catch (final IOException | TikaException e) {
            LOGGER.warn(e.getMessage(), e);
            return FileMetaData.NOTHING;
        }
    }
}
