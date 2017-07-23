package de.weltraumschaf.maconha.backend.service;

/**
 * Used to create concrete implementation based on a configured id.
 */
public interface ScanServiceFactory {
    /**
     * Spring batch based implementation.
     */
    String BATCH = "batch";
    /**
     * Thread task executor based implementation.
     */
    String THREAD = "thread";

    /**
     * Auto implemented by Spring.
     *
     * @param id must not be {@code null} or empty, either {@link #BATCH} or {@link #THREAD}
     * @return never {@code null}
     */
    ScanService create(String id);
}
