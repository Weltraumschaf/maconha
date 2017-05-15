package de.weltraumschaf.maconha.service.scan.extraction;

import de.weltraumschaf.commons.validate.Validate;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Extracts {@link FileMetaData meta data} from the file content.
 */
public final class MetaDataExtractor implements Extractor<FileMetaData> {

    private final Tika tika;

    public MetaDataExtractor() {
        this(new Tika());
    }

    MetaDataExtractor(final Tika tika) {
        super();
        this.tika = Validate.notNull(tika, "tika");
    }

    @Override
    public FileMetaData extract(final String path) throws Exception {
        final Metadata metadata = new Metadata();
        final Path file = Paths.get(path);

        try (final TikaInputStream input = TikaInputStream.get(file)) {
            final MediaType mimetype = tika.getDetector().detect(input, metadata);
            return new FileMetaData(mimetype.toString(), tika.parseToString(input));
        }
    }
}
