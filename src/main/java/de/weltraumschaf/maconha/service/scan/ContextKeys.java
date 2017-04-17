package de.weltraumschaf.maconha.service.scan;

/**
 * Provides keys used to store and retrieve data in job/step/task contexts.
 */
final class ContextKeys {
    /**
     * Under this key the files filtered by {@link FilterUnseenFilesTasklet} will be stored.
     */
    static final String UNSEEN_FILES = "unseenFiles";

    /**
     * This class only provides string constants.
     */
    private ContextKeys() {
        super();
    }
}
