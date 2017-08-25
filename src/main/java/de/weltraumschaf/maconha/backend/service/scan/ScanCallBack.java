package de.weltraumschaf.maconha.backend.service.scan;

/**
 * Clients which want to be called back must implement this interface.
 */
interface ScanCallBack {
    /**
     * Will be invoked before a scan starts.
     *
     * @param id the unique id of the scan
     */
    void beforeScan(long id);

    /**
     * Will be invoked after a scan has finished.
     *
     * @param id the unique id of the scan
     */
    void afterScan(long id);

    /**
     * Will be invoked on any error during a scan.
     *
     * @param id the unique id of the scan
     * @param e never {@code null}
     */
    void onError(long id, Exception e);
}
