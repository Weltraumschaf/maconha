package de.weltraumschaf.maconha.service.scan;

import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks whether a keyword will be accepted or not.
 */
final class MalformedKeywords implements Predicate<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MalformedKeywords.class);

    /**
     * Pattern to find if there is something not a unicode letter or number.
     *
     * http://stackoverflow.com/questions/1611979/remove-all-non-word-characters-from-a-string-in-java-leaving-accented-charact
     */
    private final Pattern NOT_ALPHA_NUM = Pattern.compile("[^\\p{L}\\p{Nd}]+");

    @Override
    public boolean test(final String keyword) {
        if (null == keyword) {
            LOGGER.debug("Ignore malformed keyword because it is null.");
            return false;
        }

        final String trimmedKeyword = keyword.trim();

        if (trimmedKeyword.isEmpty()) {
            LOGGER.debug("Ignore malformed keyword because it is empty.");
            return false;
        }

        if (NOT_ALPHA_NUM.matcher(trimmedKeyword).find()) {
            LOGGER.debug("Ignore malformed keyword '{}' because it contains non alphanumeric characters.", keyword);
            return false;
        }

        if (trimmedKeyword.charAt(0) == '0') {
            LOGGER.debug("Ignore malformed keyword '{}' because it starts with 0.", keyword);
            return false;
        }

        if (trimmedKeyword.matches("[\\d]+")) {
            return true;
        }

        if (trimmedKeyword.length() < 3) {
            LOGGER.debug("Ignore malformed keyword '{}' because it is shorter than three characters.", keyword);
            return false;
        }

        return true;
    }
}