
package de.weltraumschaf.maconha.backend.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for {@link Keyword}.
 */
public class KeywordTest {

    @Test
    @Ignore
    public void equalsAndHashCode() {
        final MediaFile mediaFileOne = new MediaFile();
        mediaFileOne.setFileHash("foo");
        final MediaFile mediaFileTwo = new MediaFile();
        mediaFileTwo.setFileHash("bar");

        EqualsVerifier
            .forClass(Keyword.class)
            .withPrefabValues(MediaFile.class, mediaFileOne, mediaFileTwo)
            .withIgnoredFields("id", "mediaFiles")
            .verify();
    }

}
