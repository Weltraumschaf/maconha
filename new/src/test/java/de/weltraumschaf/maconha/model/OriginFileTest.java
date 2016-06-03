
package de.weltraumschaf.maconha.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Tests for {@link OriginFile}.
 */
public class OriginFileTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier
            .forClass(OriginFile.class)
            .withPrefabValues(Media.class, new Media().setId(1), new Media().setId(2))
            .verify();
    }

}
