package de.weltraumschaf.maconha.backend.model;

import de.weltraumschaf.commons.validate.Validate;

/**
 * Immutable data holder for a {@link de.weltraumschaf.maconha.backend.model.entity.Keyword#literal keyword literal} and the
 * number of {@link de.weltraumschaf.maconha.backend.model.entity.MediaFile} which have this particular keyword.
 */
public final class KeywordAndNumberOfMediaFiles {
    private final String literal;
    private final long numberOfMediaFiles;

    /**
     * Dedicated constructor.
     *
     * @param literal            must not be {@code null} or empty
     * @param numberOfMediaFiles must be greater than 0
     */
    public KeywordAndNumberOfMediaFiles(final String literal, final long numberOfMediaFiles) {
        super();
        this.literal = Validate.notEmpty(literal, "literal");
        this.numberOfMediaFiles = Validate.greaterThan(numberOfMediaFiles, 0, "numberOfMediaFiles");
    }

    /**
     * Get the keyword literal.
     *
     * @return never {@code null} or empty
     */
    public String getLiteral() {
        return literal;
    }

    /**
     * Get the number of {@link de.weltraumschaf.maconha.backend.model.entity.MediaFile medai files} which have this
     * particular keyword.
     *
     * @return greater than 0
     */
    public long getNumberOfMediaFiles() {
        return numberOfMediaFiles;
    }
}
