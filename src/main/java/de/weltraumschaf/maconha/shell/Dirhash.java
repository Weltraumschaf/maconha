package de.weltraumschaf.maconha.shell;

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
     * @param baseDir must not be {@link null}
     */
    Dirhash(final Path path, final Path baseDir) {
        super(path, CMD, baseDir.toString());
    }

}
