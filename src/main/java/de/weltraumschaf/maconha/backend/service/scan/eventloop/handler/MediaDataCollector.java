package de.weltraumschaf.maconha.backend.service.scan.eventloop.handler;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.model.FileMetaData;
import de.weltraumschaf.maconha.backend.service.scan.hashing.HashedFile;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Immutable value class to carry around data from one event to another.
 */
final class MediaDataCollector {
    private final HashedFile file;
    private final Collection<String> keywords = new HashSet<>();
    private final FileMetaData metaData;

    /**
     * Constructor for external use.
     *
     * @param file must not be {@code null}
     */
    MediaDataCollector(final HashedFile file) {
        this(file, FileMetaData.NOTHING);
    }

    /**
     * Dedicated constructor.
     *
     * @param file     must not be {@code null}
     * @param metaData must not be {@code null}
     */
    private MediaDataCollector(final HashedFile file, final FileMetaData metaData) {
        super();
        this.file = Validate.notNull(file, "file");
        this.metaData = Validate.notNull(metaData, "metaData");
    }

    /**
     * Get the file.
     *
     * @return never {@code null}
     */
    HashedFile getFile() {
        return file;
    }

    /**
     * Get the meta data.
     *
     * @return never {@code null}
     */
    FileMetaData getMetaData() {
        return metaData;
    }

    /**
     * Set the meta data.
     * <p>
     * This method does not modify the called object, instead returns the new version.
     * </p>
     *
     * @param metaData must not be {@code null}
     * @return never {@code null}, new version with updated meta data
     */
    MediaDataCollector setMetaData(final FileMetaData metaData) {
        return copy(metaData);
    }

    /**
     * Get the keywords.
     *
     * @return never {@code null}, defensive copy
     */
    Collection<String> getKeywords() {
        // Does not use unmodifiable because it does not implement equals/hashcode.
        return new HashSet<>(keywords);
    }

    /**
     * Add the keywords.
     * <p>
     * This method does not modify the called object, instead returns the new version. The new keywords
     * are merged into already existing ones.
     * </p>
     *
     * @param keywords must not be {@code null}
     * @return never {@code null}, new version with updated keywords
     */
    MediaDataCollector addKeyWords(final Collection<String> keywords) {
        final MediaDataCollector newCollector = copy(metaData);
        newCollector.keywords.addAll(Validate.notNull(keywords, "keywords"));
        return newCollector;
    }

    /**
     * Sets the keywords.
     * <p>
     * This method overwrites preexisting keywords instead of merging them.
     * </p>
     *
     * @param keywords must not be {@code null}
     * @return never {@code null}, new version with updated keywords
     */
    MediaDataCollector setKeyWords(final Collection<String> keywords) {
        final MediaDataCollector newCollector = copy(metaData);
        newCollector.keywords.clear();
        newCollector.keywords.addAll(keywords);
        return newCollector;
    }

    private MediaDataCollector copy(final FileMetaData metaData) {
        final MediaDataCollector newCollector = new MediaDataCollector(file, metaData);
        newCollector.keywords.addAll(keywords);
        return newCollector;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof MediaDataCollector)) {
            return false;
        }

        final MediaDataCollector that = (MediaDataCollector) o;
        return Objects.equals(file, that.file) &&
            Objects.equals(keywords, that.keywords) &&
            Objects.equals(metaData, that.metaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, keywords, metaData);
    }

    @Override
    public String toString() {
        return "MediaDataCollector{" +
            "file=" + file +
            ", keywords=" + keywords +
            ", metaData=" + metaData +
            '}';
    }
}
