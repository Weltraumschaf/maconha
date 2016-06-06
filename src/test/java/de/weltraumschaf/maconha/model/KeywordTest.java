
package de.weltraumschaf.maconha.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Tests for {@link Keyword}.
 */
public class KeywordTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier
            .forClass(Keyword.class)
            .withPrefabValues(Media.class, new Media().setId(1), new Media().setId(2))
            .verify();
    }

}
