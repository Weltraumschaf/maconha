package de.weltraumschaf.maconha.core;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Checks whether a keyword will be accepted or not.
 */
public final class MalformedKeywords implements Predicate<String> {

    /**
     * Pattern to find if there is something not a unicode letter or number.
     *
     * http://stackoverflow.com/questions/1611979/remove-all-non-word-characters-from-a-string-in-java-leaving-accented-charact
     */
    private final Pattern NOT_ALPHA_NUM = Pattern.compile("[^\\p{L}\\p{Nd}]+");

    @Override
    public boolean test(final String keyword) {
        if (null == keyword) {
            return false;
        }

        final String trimmedKeyword = keyword.trim();

        if (trimmedKeyword.isEmpty()) {
            return false;
        }

        if (NOT_ALPHA_NUM.matcher(trimmedKeyword).find()) {
            return false;
        }

        if (trimmedKeyword.charAt(0) == '0') {
            return false;
        }

        if (trimmedKeyword.matches("[\\d]+")) {
            return true;
        }

        if (trimmedKeyword.length() < 3) {
            return false;
        }

        return true;
    }
}
