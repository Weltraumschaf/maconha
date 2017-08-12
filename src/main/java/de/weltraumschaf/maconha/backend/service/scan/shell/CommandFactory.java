package de.weltraumschaf.maconha.backend.service.scan.shell;

import java.nio.file.Path;

/**
 * Factory which provides various pre configured {@link Command shell commands}.
 */
public interface CommandFactory {
    /**
     * Create directory hash command.
     *
     * @param directory must not be {@code null}
     * @return never {@code null}, always new object
     */
    public Command dirhash(Path directory);
}
