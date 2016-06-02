package de.weltraumschaf.maconha.core;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 */
public final class FileNameExtractor {

    public Collection<String> extractKeywords(final Path inputToExtract) {
        final String absolutePath = inputToExtract.toString();
        final String cleansed = replaceMultipleWhitespacesWithOne(absolutePath.replaceAll("[/\\-_\\.]", " ").trim());
        return Arrays.asList(cleansed.split("\\s+"))
            .stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
    }

    // "foo-bar" -> "foo bar" (but "foo - bar" -> "foo - bar")
    // "FooBar" -> "Foo Bar"
    public String extractTitle(final Path inputToExtract) {
        final String absolutePath = inputToExtract.toString();
        return cleanseTitle(absolutePath.substring(
            absolutePath.lastIndexOf('/') + 1,
            absolutePath.lastIndexOf('.')));
    }

    public FileExtension extractExtension(final Path inputToExtract) {
        return Movies.forValue(FileExtension.extractExtension(inputToExtract));
    }

    private String cleanseTitle(final String title) {
        return replaceMultipleWhitespacesWithOne(title
            // Remove special characters .
            .replaceAll("_", " ")
            // Remove multiple characters with one.
            .replaceAll("-+", "-")
            .trim());
    }

    private String replaceMultipleWhitespacesWithOne(final String input) {
        return input.replaceAll("\\s+", " ");
    }

    protected String splitCamelCase(final String input) {
        if (null == input || input.trim().isEmpty()) {
            return "";
        }

        // http://stackoverflow.com/questions/2559759/how-do-i-convert-camelcase-into-human-readable-names-in-java
        return input.replaceAll(
            String.format("%s|%s|%s",
                "(?<=[A-Z])(?=[A-Z][a-z])", // UC behind me, UC followed by LC in front of me.
                "(?<=[^A-Z])(?=[A-Z])", // non-UC behind me, UC in front of me.
                "(?<=[A-Za-z])(?=[^A-Za-z])" // Letter behind me, non-letter in front of me.
            ),
            " "
        );
    }
}
