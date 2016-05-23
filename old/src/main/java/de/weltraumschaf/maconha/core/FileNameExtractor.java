package de.weltraumschaf.maconha.core;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 */
final class FileNameExtractor {

    public Result process(final Path inputToExtract) {
        final Result extracted = new Result();
        extracted.keywords = extractKeywords(inputToExtract);
        extracted.title = extractTitle(inputToExtract);
        extracted.extension = extractExtension(inputToExtract);
        return extracted;
    }

    Collection<String> extractKeywords(final Path inputToExtract) {
        final String absolutePath = inputToExtract.toString();
        final String cleansed = replaceMultipleWhitespacesWithOne(absolutePath.replaceAll("[/\\-_\\.]", " ").trim());
        return Arrays.asList(cleansed.split("\\s+"))
            .stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
    }

    String extractTitle(final Path inputToExtract) {
        final String absolutePath = inputToExtract.toString();
        return cleanseTitle(absolutePath.substring(
            absolutePath.lastIndexOf('/') + 1,
            absolutePath.lastIndexOf('.')));
    }

    FileExtension extractExtension(final Path inputToExtract) {
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
        return input.replaceAll(" +", " ");
    }

    public static final class Result {
        private Collection<String> keywords = Collections.emptyList();
        private String title = "";
        private FileExtension extension = FileExtension.NONE;

        public String getTitle() {
            return title;
        }

        public FileExtension getExtension() {
            return extension;
        }

        public Collection<String> getKeywords() {
            return Collections.unmodifiableCollection(keywords);
        }


    }
}
