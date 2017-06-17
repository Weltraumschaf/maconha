package de.weltraumschaf.maconha.service.scanstatus;

import de.weltraumschaf.maconha.service.ScanService.ScanStatus;

import java.io.Reader;
import java.util.Collection;

/**
 * Helper to de-/serialize statuses.
 */
public interface StatusSerializer {
    /**
     * Serializes the given collection of statuses to a given writer.
     *
     * @param statuses must not be {@code null}
     * @param writer   must not be {@code null}
     */
    void serialize(final Collection<ScanStatus> statuses, final Appendable writer);

    /**
     * Deserialize statuses from given reader.
     *
     * @param reader must not be {@code null}
     * @return never {@code null}
     */
    Collection<ScanStatus> deserialize(final Reader reader);
}
