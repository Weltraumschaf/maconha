package de.weltraumschaf.maconha.shell;

import de.weltraumschaf.commons.validate.Validate;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Common base class for commands.
 * <p>
 * See this <a href="http://examples.javacodegeeks.com/core-java/lang/processbuilder/java-lang-processbuilder-example/">
 * tutorial</a> for more information.
 * </p>
 */
abstract class BaseCommand implements Command {

    private final String path;

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
     * @param path must not be {@code null} or empty
     * @param command must not be {@code null} or empty
     * @param arguments must not be {@code null}
     */
    BaseCommand(final String path, final String command, final String arguments) {
        super();
        this.path = Validate.notEmpty(path, "path");
        this.command = Validate.notEmpty(command, "command");
        this.arguments = Validate.notNull(arguments, "arguments");
    }

    final void setBuilder(final ProcessBuilderWrapper builder) {
        this.builder = Validate.notNull(builder, "builder");
    }

    @Override
    public final Result execute() throws IOException, InterruptedException {
        final Process process = builder.start(path + '/' + command, arguments);
        final IoThreadHandler stdout = new IoThreadHandler(process.getInputStream());
        stdout.start();
        final IoThreadHandler stderr = new IoThreadHandler(process.getErrorStream());
        stderr.start();
        int errCode = process.waitFor();
        stdout.join();
        stderr.join();

        return new Result(errCode, stdout.getOutput(), stderr.getOutput());
    }

    /**
     * Reads the output from the process in separate thread.
     */
    private static final class IoThreadHandler extends Thread {

        /**
         * Input to read from.
         */
        private final InputStream input;
        /**
         * Collects the read input.
         */
        private final StringBuilder buffer = new StringBuilder();

        /**
         * Dedicated constructor.
         *
         * @param input must not be {@code null}
         */
        IoThreadHandler(final InputStream input) {
            super();
            this.input = Validate.notNull(input, "input");
        }

        @Override
        public void run() {
            try (final Scanner br = new Scanner(new InputStreamReader(input))) {
                while (br.hasNextLine()) {
                    buffer.append(br.nextLine()).append('\n');
                }
            }
        }

        /**
         * Get the collected output.
         *
         * @return never {@code null}
         */
        String getOutput() {
            return buffer.toString();
        }
    }
}
