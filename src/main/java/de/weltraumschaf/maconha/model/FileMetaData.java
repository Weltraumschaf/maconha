package de.weltraumschaf.maconha.model;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.service.mediafile.MetaDataExtractor;

import java.util.Objects;

/**
 * Immutable value object whoch contains the meta data found by {@link MetaDataExtractor}.
 */
public final class FileMetaData {

    public static final FileMetaData NOTHING = new FileMetaData("unknown", "");

    private final String mime;
    private final String data;

    /**
     * Dedicated constructor.
     *
     * @param mime must not be {@code null} nor empty
     * @param data must not be {@code null}
     */
    public FileMetaData(final String mime, final String data) {
        super();
        this.mime = Validate.notEmpty(mime, "mime");
        this.data = Validate.notNull(data, "data");
    }

    /**
     * Get the mime type string.
     *
     * @return never {@code null} nor empty
     */
    public String getMime() {
        return mime;
    }

    /**
     * Get the meta data as new line separated string.
     *
     * @return never {@code null} nor empty
     */
    public String getData() {
        return data;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof FileMetaData)) {
            return false;
        }

        final FileMetaData fileMetaData = (FileMetaData) o;
        return Objects.equals(mime, fileMetaData.mime) &&
            Objects.equals(data, fileMetaData.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mime, data);
    }

    @Override
    public String toString() {
        return "FileMetaData{" +
            "mime='" + mime + '\'' +
            ", data='" + data + '\'' +
            '}';
    }
}
