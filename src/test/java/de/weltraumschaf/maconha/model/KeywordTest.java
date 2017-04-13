
package de.weltraumschaf.maconha.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Tests for {@link Keyword}.
 */
public class KeywordTest {

    @Test
    public void equalsAndHashCode() {
        final Media mediaOne = new Media();
        mediaOne.setId(1);
        final Media mediaTwo = new Media();
        mediaTwo.setId(2);

        EqualsVerifier
            .forClass(Keyword.class)
            .withPrefabValues(Media.class, mediaOne, mediaTwo)
            .withIgnoredFields("medias")
            .verify();
    }

}
