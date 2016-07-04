package de.weltraumschaf.maconha.shell;

import de.weltraumschaf.commons.validate.Validate;

/**
 * Factory which provides various pre configured {@link Command shell commands}.
 */
public final class Commands {

    private final String path;

    public Commands(final String path) {
        super();
        this.path = Validate.notEmpty(path, "path");
    }


    /**
     *
     * @param directory must not be {@code null} or empty
     * @return never {@code null}, always new object
     */
    public Command dirhash(final String directory) {
        return new Dirhash(path, Validate.notEmpty(directory, "directory"));
    }
}
