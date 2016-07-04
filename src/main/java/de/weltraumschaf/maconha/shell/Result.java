package de.weltraumschaf.maconha.shell;

import de.weltraumschaf.commons.validate.Validate;
import java.util.Objects;

/**
 * Result of a {@link Command shell command}.
 */
public final class Result {

    /**
     * Exit code of command.
     */
    private final int exitCode;
    /**
     * Command output from STDOUT.
     */
    private final String stdout;
    /**
     * Command output from STDERR.
     */
    private final String stderr;

    /**
     * Hidden because it is only generated by {@link Command}.
     *
     * @param exitCode any int
     * @param stdout must not be {@code null}
     * @param stderr must not be {@code null}
     */
    Result(final int exitCode,  final String stdout, final String stderr) {
        super();
        this.exitCode = exitCode;
        this.stdout = Validate.notNull(stdout, "stdout");
        this.stderr = Validate.notNull(stderr, "stderr");
    }

    /**
     * Get the commands exit code.
     *
     * @return any int
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * Short hand for {@code getExitCode() == 0}.
     *
     * @return {@code false} if command had an error, else {@code true}
     */
    public boolean isSuccessful() {
        return exitCode == 0;
    }

    public boolean isFailed() {
        return !isSuccessful();
    }

    /**
     * Get the output from STDOUT.
     *
     * @return never {@code null}
     */
    public String getStdout() {
        return stdout;
    }

    /**
     * Get the output from STDERR.
     *
     * @return never {@code null}
     */
    public String getStderr() {
        return stderr;
    }

    @Override
    public int hashCode() {
        return Objects.hash(exitCode, stdout, stderr);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Result)) {
            return false;
        }

        final Result other = (Result) obj;
        return this.exitCode == other.exitCode
            && Objects.equals(this.stdout, other.stdout)
            && Objects.equals(this.stderr, other.stderr);
    }

    @Override
    public String toString() {
        return "Result{"
            + "exitCode=" + exitCode + ", "
            + "stdout=" + stdout + ", "
            + "stderr=" + stderr
            + '}';
    }

}
