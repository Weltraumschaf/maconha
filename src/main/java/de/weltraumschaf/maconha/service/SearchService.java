package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.MediaFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * This service provides the business logic to find {@link MediaFile media files}.
 */
public interface SearchService {
    /**
     * Find media files by keywords.
     *
     * @param keywords not {@code null}
     * @return never {@code null}, maybe empty
     */
    Collection<MediaFile> forKeywords(Collection<String> keywords);

    /**
     * Download a file by its relative name.
     * <p>
     * Produces 404 error response if not found.
     * </p>
     *
     * @param relativeFilename not {@code null} or empty
     * @param response not {@code null}
     * @throws IOException if file can't be read local or written to response
     */
    void downloadFile(String relativeFilename, HttpServletResponse response) throws IOException;
}
