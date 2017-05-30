package de.weltraumschaf.maconha.service.mediafile;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Helper class to make useful string manipulations to extract keywords.
 */
final class StringManipulator {

    /**
     * Used as default return value to avoid {@code null}.
     */
    private static final String DEFAULT = "";
    /**
     * Used as default replacement.
     */
    private static final String DEFAULT_REPLACEMENT = " ";
    /**
     * Regex used to match special characters.
     */
    private static final String REGEX_SPECIAL_CHARS = "[/\\\\\\-_.,:;+*&%$?{}()\\[\\]'=<>|~#^]";
    /**
     * Regex character class for unicode upper case letters.
     */
    private static final String UC_CLASS = "\\p{Upper}";
    /**
     * Regex character class for unicode lower case letters.
     */
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
    /**
     * Composed Regex to split camel case with enabled unicode support {@literal (?U)}.
     */
    private static final String SPLIT_CAMEL_CASE = String.format(
        "(?U)%s|%s|%s",
        UC_BEHIND_ME, NON_UC_BEHIND_ME, LETTER_BEHIND_ME);

    /**
     * Removes everything from the last dot from the given string.
     * <p>
     * Examples:
     * </p>
     * <pre>
     * null          -> ""
     * ""            -> ""
     * "   "         -> ""
     * "foo"         -> "foo"
     * "foo.bar"     -> "foo"
     * "foo.bar.baz" -> "foo.bar"
     * </pre>
     *
     * @param input may be {@code null} or blank
     * @return never {@code null}
     */
    String removeFileExtension(final String input) {
        if (isNullOrBlank(input)) {
            return DEFAULT;
        }

        final int lasPositionOfDot = input.lastIndexOf('.');

        if (lasPositionOfDot < 0) {
            return input;
        }

        return input.substring(0, lasPositionOfDot);
    }

    /**
     * Replaces special characters by a single whitespace.
     * <p>
     * Special characters replaces are {@literal / \ - _ .}. Multiple consecutive special characters will lead to
     * multiple white spaces. To reduce them you can use {@link #replaceMultipleWhitespacesWithOne(String)}.
     * </p>
     *
     * @param input may be {@code null} or blank
     * @return never {@code null}
     */
    String replaceSpecialCharacters(final String input) {
        if (isNullOrBlank(input)) {
            return DEFAULT;
        }

        return input.replaceAll(REGEX_SPECIAL_CHARS, DEFAULT_REPLACEMENT);
    }

    /**
     * Replaces multiple consecutive whitespaces by a single whitespace.
     *
     * @param input may be {@code null} or blank
     * @return never {@code null}
     */
    String replaceMultipleWhitespacesWithOne(final String input) {
        if (isNullOrBlank(input)) {
            return DEFAULT;
        }

        return input.replaceAll("\\s+", DEFAULT_REPLACEMENT);
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

    Collection<String> splitIntoLines(final String input) {
        if (isNullOrBlank(input)) {
            return Collections.emptyList();
        }

        return Arrays.asList(input.split("\\r?\\n"));
    }

    Collection<String> splitIntoWords(final String input) {
        if (isNullOrBlank(input)) {
            return Collections.emptyList();
        }

        return Arrays.asList(input.trim().split("\\s"));
    }

    private boolean isNullOrBlank(final String input) {
        return null == input || input.trim().isEmpty();
    }
}
