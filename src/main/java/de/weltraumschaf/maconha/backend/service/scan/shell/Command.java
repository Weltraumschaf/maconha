package de.weltraumschaf.maconha.backend.service.scan.shell;

import java.io.IOException;

/**
 * A executable command.
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

}
