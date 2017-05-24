package de.weltraumschaf.maconha.shell;

import java.io.IOException;

/**
 */
final class DefaultProcessBuilderWrapper implements ProcessBuilderWrapper {

    @Override
    public Process start(String ... command) throws IOException {
        return new ProcessBuilder(command).start();
    }
}
