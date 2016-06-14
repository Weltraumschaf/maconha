package de.weltraumschaf.maconha.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 */
public final class IgnoredKeywords implements Predicate<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IgnoredKeywords.class);
    private static final Collection<String> IGNORED_KEYWORDS = Arrays.asList(
        "i", "a", "an", "ve", "the", "this", "that", "who");

    @Override
    public boolean test(final String keyword) {
        if (IGNORED_KEYWORDS.contains(keyword)) {
            LOGGER.debug("Ignore keyword '{}'.", keyword);
            return false;
        }

        return true;
    }

}
