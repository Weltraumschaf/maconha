package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.commons.validate.Validate;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a file with a hash of its content.
 */
final class HashedFile implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String hash;
    private final String file;

    /**
     * Dedicated constructor.
     *
     * @param hash must not be {@code null} or empty
     * @param file must not be {@code null} or empty
     */
    HashedFile(final String hash, final String file) {
        super();
        this.hash = Validate.notEmpty(hash, "hash");
        this.file = Validate.notEmpty(file, "file");
    }

    /**
     * Get the hash.
     *
     * @return never {@code null} or empty
     */
    public String getHash() {
        return hash;
    }

    /**
     * Get the file name.
     *
     * @return never {@code null} or empty
     */
    public String getFile() {
        return file;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, file);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof HashedFile)) {
            return false;
        }

        final HashedFile other = (HashedFile) obj;
        return Objects.equals(this.hash, other.hash)
            && Objects.equals(this.file, other.file);
    }

    @Override
    public String toString() {
        return "HashedFile{" + "hash=" + hash + ", file=" + file + '}';
    }

}
