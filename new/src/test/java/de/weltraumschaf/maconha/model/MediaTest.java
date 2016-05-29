
package de.weltraumschaf.maconha.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Tests for {@link Media}.
 */
public class MediaTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(Media.class).verify();
    }

}
