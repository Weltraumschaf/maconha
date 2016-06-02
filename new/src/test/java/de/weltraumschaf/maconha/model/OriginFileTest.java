
package de.weltraumschaf.maconha.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link OriginFile}.
 */
public class OriginFileTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(OriginFile.class).verify();
    }

}