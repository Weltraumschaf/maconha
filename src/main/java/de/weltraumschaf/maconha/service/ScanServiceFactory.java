package de.weltraumschaf.maconha.service;

/**
 * Used to create concrete implementation based on a configured id.
 */
public interface ScanServiceFactory {
    String BATCH = "batch";
    String THREAD = "thread";

    /**
     * Auto implemented by Spring.
     *
     * @param id must not be {@code null} or empty, either {@link #BATCH} or {@link #THREAD}
     * @return never {@code null}
     */
    ScanService create(String id);
}