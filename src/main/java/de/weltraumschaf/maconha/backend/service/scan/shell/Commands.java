package de.weltraumschaf.maconha.backend.service.scan.shell;

import de.weltraumschaf.commons.validate.Validate;

import java.nio.file.Path;

/**
 * Default implementation.
 */
public final class Commands implements CommandFactory{

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

    @Override
    public Command dirhash(final Path directory) {
        return new Dirhash(path, Validate.notNull(directory, "directory"));
    }
}
