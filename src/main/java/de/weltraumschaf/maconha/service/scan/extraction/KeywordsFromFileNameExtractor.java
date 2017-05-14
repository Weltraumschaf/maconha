package de.weltraumschaf.maconha.service.scan.extraction;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Utility class to extract information from file names.
 */
public final class KeywordsFromFileNameExtractor implements KeywordExtractor<Collection<String>> {

    private final StringManipulator manipulator = new StringManipulator();

    @Override
    public Collection<String> extract(final String path) {
        final String pathWithoutExtension = manipulator.removeFileExtension(path.trim());
        final String pathWithoutSpecialChars = manipulator.replaceSpecialCharacters(pathWithoutExtension);
        final String[] keywords = pathWithoutSpecialChars.trim().split("\\s+");

        return Arrays.stream(keywords)
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
    }

}
