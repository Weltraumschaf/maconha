package de.weltraumschaf.maconha.shell;

import de.weltraumschaf.commons.validate.Validate;
import java.nio.file.Path;

/**
 * Factory which provides various pre configured {@link Command shell commands}.
 */
public final class Commands {

    /**
     * PAth to the command.
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

}
