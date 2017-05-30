package de.weltraumschaf.maconha.service.mediafile;

import de.weltraumschaf.maconha.model.FileMetaData;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Extracts keywords from a {@link FileMetaData#getData() meta data string}.
 */
public final class KeywordsFromMetaDataExtractor implements KeywordExtractor {

    private final StringManipulator manipulator = new StringManipulator();

    @Override
    public Collection<String> extract(final String input) {
        return manipulator.splitIntoLines(input).stream()
            .map(this::processLine)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    private Collection<String> processLine(final String line) {
        String processed = manipulator.replaceSpecialCharacters(line);
        processed = manipulator.splitCamelCase(processed);
        processed = manipulator.replaceMultipleWhitespacesWithOne(processed);

        return manipulator.splitIntoWords(processed)
            .stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
    }

}
