package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.commons.validate.Validate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Reads a file with hash codes of files.
 * <p>
 * The format of the file is: {@code (HASH SPACES FILE\n)*}
 * </p>
 * <p>
 * Example:
 * </p>
 * <pre>
 * 2f04335a0b0a96b50f9e19de9fa5a4aac2c0cc42e474bf3b9401790e33e166cb  Animation/android_207_HQ.mp4
 * 37055372aa9ab1679aeab43d9534fe65d0b217c84679b1ff43274e2b4a58a308  Animation/animusic/Animusic-AcousticCurves.wmv
 * 3a77f9b6e3aa5fed770f94b3aadcd284c8d1f8dc0ced62a018ab6671ab73c8f9  Animation/animusic/Animusic-AquaHarp.wmv
 * </pre>
 */
final class HashFileReader {

    /**
     * Read in a checksum file.
     *
     * @param checksums must not be {@code null}
     * @return never {@code null}, always new instance
     * @throws IOException if checksum file can't be read
     */
    public Set<HashedFile> read(final Path checksums) throws IOException {
        return Files.readAllLines(Validate.notNull(checksums, "checksums"))
            .stream()
            .map(line -> {
                final int splitPos = line.indexOf(' ');
                final String hash = line.substring(0, splitPos).trim();
                final String file = line.substring(splitPos).trim();
                return new HashedFile(hash, file.replace("//", "/"));
            }).collect(Collectors.toSet());
    }

}
