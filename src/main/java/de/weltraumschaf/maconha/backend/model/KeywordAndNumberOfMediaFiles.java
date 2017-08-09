package de.weltraumschaf.maconha.backend.model;

/**
 *
 */
public final class KeywordAndNumberOfMediaFiles {
    private final String literal;
    private final long numberOfMediaFiles;

    public KeywordAndNumberOfMediaFiles(final String literal, final long numberOfMediaFiles) {
        super();
        this.literal = literal;
        this.numberOfMediaFiles = numberOfMediaFiles;
    }

    public String getLiteral() {
        return literal;
    }

    public long getNumberOfMediaFiles() {
        return numberOfMediaFiles;
    }
}
