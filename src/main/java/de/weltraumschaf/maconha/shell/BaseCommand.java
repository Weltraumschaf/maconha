package de.weltraumschaf.maconha.shell;

import de.weltraumschaf.commons.validate.Validate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Common base class for commands.
 * <p>
 * See this <a href="http://examples.javacodegeeks.com/core-java/lang/processbuilder/java-lang-processbuilder-example/">
 * tutorial</a> for more information.
 * </p>
 */
abstract class BaseCommand implements Command {

    /**
     * Where to command is.
     */
    private final Path commandDir;
    /**
     * Command to execute.
     */
    private final String command;
    /**
     * Command arguments.
     */
    private final String arguments;
    /**
     * Abstracts the process builder for testability.
     */
    private ProcessBuilderWrapper builder = new DefaultProcessBuilderWrapper();

    /**
     * Dedicated constructor.
     *
     * @param commandDir where the command is, must not be {@code null}
     * @param command the command itself, must not be {@code null} or empty
     * @param arguments must not be {@code null}, maybe empty
     */
    BaseCommand(final Path commandDir, final String command, final String arguments) {
        super();
        this.commandDir = Validate.notNull(commandDir, "commandDir");
        this.command = Validate.notEmpty(command, "command");
        this.arguments = Validate.notNull(arguments, "arguments");
    }

    final void setBuilder(final ProcessBuilderWrapper builder) {
        this.builder = Validate.notNull(builder, "builder");
    }

    @Override
    public final Result execute() throws IOException, InterruptedException {
        final Process process = builder.start(commandDir.resolve(command).toString(), arguments);
        final InputThreadHandler stdout = new InputThreadHandler(process.getInputStream());
        stdout.start();
        final InputThreadHandler stderr = new InputThreadHandler(process.getErrorStream());
        stderr.start();
        int errCode = process.waitFor();
        stdout.join();
        stderr.join();

        return new Result(errCode, stdout.getOutput(), stderr.getOutput());
    }

}
