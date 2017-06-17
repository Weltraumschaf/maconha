package de.weltraumschaf.maconha.service.scan.batch;

/**
 * Provides keys used to store and retrieve data in job/step/task contexts.
 */
interface ContextKeys {
    /**
     * Under this key the files filtered by {@link FilterUnseenFilesTasklet} will be stored.
     */
    String UNSEEN_FILES = "unseenFiles";
}
