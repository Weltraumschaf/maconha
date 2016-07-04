package de.weltraumschaf.maconha.shell;

import java.io.IOException;

/**
 */
interface ProcessBuilderWrapper {

    Process start(String ... command) throws IOException;
}
