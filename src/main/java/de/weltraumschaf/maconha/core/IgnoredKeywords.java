package de.weltraumschaf.maconha.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;


/**
 */
public final class IgnoredKeywords implements Predicate<String> {

    private static final Collection<String> IGNORED_KEYWORDS = Arrays.asList(
        "i", "a", "an", "ve", "the", "this", "that", "who");

    @Override
    public boolean test(final String keyword) {
        return !IGNORED_KEYWORDS.contains(keyword);
    }

}
