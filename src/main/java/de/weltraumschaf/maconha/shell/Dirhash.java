package de.weltraumschaf.maconha.shell;

/**
 * Executes the {@literal ls} command.
 */
final class Dirhash extends BaseCommand{

    /**
     * Literal shell command.
     */
    private static final String CMD  = "dirhash";

    /**
     * Dedicated constructor.
     *
     * @param path must not be {@code null} or empty
     * @param arguments must not be {@link null}
     */
    Dirhash(final String path, final String arguments) {
        super(path, CMD, arguments);
    }

}
