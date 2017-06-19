package de.weltraumschaf.maconha.shell;

import de.weltraumschaf.commons.validate.Validate;

import java.nio.file.Path;

/**
 * Factory which provides various pre configured {@link Command shell commands}.
 */
public final class Commands {

    /**
     * Path to the command.
     */
    private final Path path;

    /**
     * Dedicated constructor.
     *
     * @param path must not be {@code null}
     */
    public Commands(final Path path) {
        super();
        this.path = Validate.notNull(path, "path");
    }

    /**
     * Create directory hash command.
     *
     * @param directory must not be {@code null}
     * @return never {@code null}, always new object
     */
    public Command dirhash(final Path directory) {
        return new Dirhash(path, Validate.notNull(directory, "directory"));
    }
}
