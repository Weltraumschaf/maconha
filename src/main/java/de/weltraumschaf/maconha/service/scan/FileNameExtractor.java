package de.weltraumschaf.maconha.service.scan;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Utility class to extract information from file names.
 */
final class FileNameExtractor {

    private static final String UC_CLASS = "\\p{Upper}";
    private static final String LC_CLASS = "\\p{Lower}";

    /**
     * UC behind me, UC followed by LC in front of me.
     */
    private static final String UC_BEHIND_ME = String.format(
        "(?<=[%s])(?=[%s][%s])",
        UC_CLASS, UC_CLASS, LC_CLASS);
    /**
     * Non-UC behind me, UC in front of me.
     */
    private static final String NON_UC_BEHIND_ME = String.format(
        "(?<=[^%s])(?=[%s])",
        UC_CLASS, UC_CLASS);
    /**
     * Letter behind me, non-letter in front of me.
     */
    private static final String LETTER_BEHIND_ME = String.format(
        "(?<=[%s%s])(?=[^%s%s])",
        UC_CLASS, LC_CLASS, UC_CLASS, LC_CLASS);
    private static final String SPLIT_CAMEL_CASE = String.format(
        "(?U)%s|%s|%s",
        UC_BEHIND_ME, NON_UC_BEHIND_ME, LETTER_BEHIND_ME);

    Collection<String> extractKeywords(final String path) {
        final String pathWithoutExtension = path.substring(0, path.lastIndexOf('.'));
        final String pathWithoutSpecialChars = pathWithoutExtension.replaceAll("[/\\-_\\.]", " ");
        final String cleansed = cleanseTitle(pathWithoutSpecialChars.trim());

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

    /**
     * Splits a given string by <a href="http://stackoverflow.com/questions/2559759/how-do-i-convert-camelcase-into-human-readable-names-in-java">
     * upper case characters</a>.
     * <p>
     * It uses zero-length matching regex with lookbehind and lookforward to find where to insert spaces.
     * it supports <a href="http://stackoverflow.com/questions/4304928/unicode-equivalents-for-w-and-b-in-java-regular-expressions">
     * unicode letters</a>.
     * </p>
     *
     * @param input may be {@code null} or blank
     * @return never {@code null}
     */
    String splitCamelCase(final String input) {
        if (isNullOrBlank(input)) {
            return "";
        }

        return input.replaceAll(SPLIT_CAMEL_CASE, " ");
    }

    String replaceAndSplitDashes(final String input) {
        if (isNullOrBlank(input)) {
            return "";
        }

        return input.replaceAll("(?<=\\S)-(?=\\S)", " ");
    }

    private boolean isNullOrBlank(final String input) {
        return null == input || input.trim().isEmpty();
    }
}
