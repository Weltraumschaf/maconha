
package de.weltraumschaf.maconha.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Tests for {@link Keyword}.
 */
public class KeywordTest {

    @Test
    public void equalsAndHashCode() {
        final MediaFile mediaFileOne = new MediaFile();
        mediaFileOne.setId(1);
        final MediaFile mediaFileTwo = new MediaFile();
        mediaFileTwo.setId(2);

        EqualsVerifier
            .forClass(Keyword.class)
            .withPrefabValues(MediaFile.class, mediaFileOne, mediaFileTwo)
            .withIgnoredFields("mediaFiles")
            .verify();
    }

}
