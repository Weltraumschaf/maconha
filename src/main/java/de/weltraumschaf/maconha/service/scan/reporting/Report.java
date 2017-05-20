package de.weltraumschaf.maconha.service.scan.reporting;

import de.weltraumschaf.commons.validate.Validate;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 */
public final class Report {
    private final BufferedWriter logFile;

    public Report(final Path logFile) throws IOException {
        super();
        this.logFile = Files.newBufferedWriter(Validate.notNull(logFile, "logFile"));
    }

    public void log(final MessageType type, final String message, final Object ... messageArguments) throws IOException {
        logFile.write(String.format("%s: ", type));
        logFile.write(String.format(message, messageArguments));
    }

    public void close() throws IOException {
        logFile.flush();
        logFile.close();
    }
}
