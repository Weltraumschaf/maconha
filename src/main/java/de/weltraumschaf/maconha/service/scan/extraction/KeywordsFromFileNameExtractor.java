package de.weltraumschaf.maconha.service.scan.extraction;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Utility class to extract information from file names.
 */
public final class KeywordsFromFileNameExtractor implements KeywordExtractor {

    private final StringManipulator manipulator = new StringManipulator();

    @Override
    public Collection<String> extract(final String path) {
        String processed = manipulator.removeFileExtension(path.trim());
        processed = manipulator.replaceSpecialCharacters(processed);
        processed = manipulator.splitCamelCase(processed);
        processed = manipulator.replaceMultipleWhitespacesWithOne(processed);

        return manipulator.splitIntoWords(processed)
            .stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
    }

}
