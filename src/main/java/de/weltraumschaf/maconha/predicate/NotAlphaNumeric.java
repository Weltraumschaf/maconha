package de.weltraumschaf.maconha.predicate;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Tests if a given string contains non alpha numeric characters.
 */
public final class NotAlphaNumeric implements Predicate<String> {
    /**
     * Pattern to find if there is something not a unicode letter or number.
     * <p>
     * http://stackoverflow.com/questions/1611979/remove-all-non-word-characters-from-a-string-in-java-leaving-accented-charact
     * </p>
     */
    private final Pattern NOT_ALPHA_NUM = Pattern.compile("[^\\p{L}\\p{Nd}]+");

    @Override
    public boolean test(final String s) {
        if (s == null || s.trim().isEmpty()) {
            return true;
        }

        return NOT_ALPHA_NUM.matcher(s.trim()).find();
    }
}
