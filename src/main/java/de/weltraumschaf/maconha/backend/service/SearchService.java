package de.weltraumschaf.maconha.backend.service;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.model.entity.MediaFile;
import de.weltraumschaf.maconha.backend.model.MediaType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

/**
 * This service provides the business logic to find {@link MediaFile media files}.
 */
public interface SearchService {
    /**
     * Find media files by keywords.
     *
     * @param keywords not {@code null}
     * @param types not {@code null}
     * @return never {@code null}, maybe empty
     */
    Collection<MediaFile> forKeywords(Collection<String> keywords, Collection<MediaType> types);

    /**
     * Download a file by its relative name.
     * <p>
     * Produces 404 error response if not found.
     * </p>
     *
     * @param relativeFilename not {@code null} or empty
     * @return never {@code null}
     * @throws IOException if file can't be read local or written to response
     */
    Download downloadFile(String relativeFilename) throws IOException;

    /**
     * Downloadable file.
     */
    final class Download {
        private final Path file;
        private final String format;

        /**
         * Dedicated constructor.
         *
         * @param file   must not be {@code null}
         * @param format must not be {@code null}
         */
        public Download(final Path file, final String format) {
            super();
            this.file = Validate.notNull(file, "file");
            this.format = Validate.notNull(format, "format");
        }

        /**
         * Get the file to download.
         *
         * @return never {@code null}
         */
        public Path getFile() {
            return file;
        }

        /**
         * Get the file format.
         *
         * @return never {@code null}
         */
        public String getFormat() {
            return format;
        }
    }
}
