package de.weltraumschaf.maconha.service.scan;

import de.weltraumschaf.maconha.model.FileExtension;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Utility class to extract information from file names.
 */
final class FileNameExtractor {

    Collection<String> extractKeywords(final String path) {
        final String cleansed = cleanseTitle(
            path.substring(0, path.lastIndexOf('.')).replaceAll("[/\\-_\\.]", " ").trim());
        return Arrays.stream(cleansed.split("\\s+"))
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
    }

    String extractTitle(final String path) {
        return cleanseTitle(path.substring(
            path.lastIndexOf('/') + 1,
            path.lastIndexOf('.')));
    }

    private String cleanseTitle(final String title) {
        String cleansed = title
            .replaceAll("_", " ")
            .replaceAll("-+", "-")
            .trim();
        cleansed = replaceAndSplitDashes(cleansed);
        cleansed = splitCamelCase(cleansed);
        return replaceMultipleWhitespacesWithOne(cleansed);
    }

    private String replaceMultipleWhitespacesWithOne(final String input) {
        return input.replaceAll("\\s+", " ");
    }

    String splitCamelCase(final String input) {
        if (isNullOrEmpty(input)) {
            return "";
        }

        // http://stackoverflow.com/questions/2559759/how-do-i-convert-camelcase-into-human-readable-names-in-java
        return input.replaceAll(
            String.format("%s|%s|%s",
                "(?<=[A-ZÄÖÜ])(?=[A-ZÄÖÜ][a-zäöü])", // UC behind me, UC followed by LC in front of me.
                "(?<=[^A-ZÄÖÜ])(?=[A-ZÄÖÜ])", // non-UC behind me, UC in front of me.
                "(?<=[A-ZÄÖÜa-zäöü])(?=[^A-ZÄÖÜa-zäöü])" // Letter behind me, non-letter in front of me.
            ),
            " "
        );
    }

    String replaceAndSplitDashes(final String input) {
        if (isNullOrEmpty(input)) {
            return "";
        }

        return input.replaceAll("(?<=\\S)-(?=\\S)", " ");
    }

    private boolean isNullOrEmpty(final String input) {
        return null == input || input.trim().isEmpty();
    }
}
