package de.weltraumschaf.maconha.backend.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Tests for {@link FileMetaData}.
 */
public final class FileMetaDataTest {
    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(FileMetaData.class).verify();
    }
}
