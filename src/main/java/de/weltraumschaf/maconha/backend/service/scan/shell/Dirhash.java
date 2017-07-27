package de.weltraumschaf.maconha.backend.service.scan.shell;

import de.weltraumschaf.commons.validate.Validate;

import java.nio.file.Path;

/**
 * Executes the {@literal dirhash} command.
 */
final class Dirhash extends BaseCommand {

    /**
     * Literal shell command.
     */
    private static final String CMD = "dirhash";

    /**
     * Dedicated constructor.
     *
     * @param path must not be {@code null}
     * @param directory to hash files, must not be {@code null}
     */
    Dirhash(final Path path, final Path directory) {
        super(path, CMD, Validate.notNull(directory, "directory").toString());
    }

}
