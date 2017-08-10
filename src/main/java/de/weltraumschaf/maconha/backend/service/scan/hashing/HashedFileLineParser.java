package de.weltraumschaf.maconha.backend.service.scan.hashing;

/**
 *
 */
public final class HashedFileLineParser {
    public HashedFile parse(final String line) {
        final int splitPos = line.indexOf(' ');
        final String hash = line.substring(0, splitPos).trim();
        final String file = line.substring(splitPos).trim();
        return new HashedFile(hash, file.replace("//", "/"));
    }
}
