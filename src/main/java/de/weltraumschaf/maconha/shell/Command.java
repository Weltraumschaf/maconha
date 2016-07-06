package de.weltraumschaf.maconha.shell;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Shell command.
 */
public interface Command {

    /**
     * Executes the command.
     *
     * @return never {@code null}
     * @throws IOException on any IO error
     * @throws InterruptedException on any thread error
     */
    Result execute() throws IOException, InterruptedException;

    Path getPath();

    String getArguments();
}
