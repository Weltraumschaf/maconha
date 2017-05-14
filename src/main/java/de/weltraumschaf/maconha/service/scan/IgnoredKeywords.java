package de.weltraumschaf.maconha.service.scan;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Predicate which tests if a keyword is ignored or or not.
 */
public final class IgnoredKeywords implements Predicate<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IgnoredKeywords.class);
    static final Collection<String> IGNORED_KEYWORDS = Arrays.asList(
        "i", "a", "an", "ve", "the", "this", "that", "who");

    @Override
    public boolean test(final String keyword) {
        if (null == keyword) {
            LOGGER.debug("Ignore null as keyword.");
            return false;
        }

        if (keyword.trim().isEmpty()) {
            LOGGER.debug("Ignore blank keyword.");
            return false;
        }

        final String normalized = keyword.trim().toLowerCase();
        if (IGNORED_KEYWORDS.contains(normalized)) {
            LOGGER.debug("Ignore keyword '{}'.", keyword);
            return false;
        }

        return true;
    }

}
