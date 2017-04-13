
package de.weltraumschaf.maconha.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * Tests for {@link OriginFile}.
 */
public class OriginFileTest {

    @Test
    public void equalsAndHashCode() {
        final Media mediaOne = new Media();
        mediaOne.setId(1);
        final Media mediaTwo = new Media();
        mediaTwo.setId(2);

        EqualsVerifier
            .forClass(OriginFile.class)
            .withPrefabValues(Media.class, mediaOne, mediaTwo)
            .withIgnoredFields("imported")
            .verify();
    }

    @Test
    public void setImported() {
        final OriginFile sut = new OriginFile();
        final Media media = new Media();

        sut.setImported(media);

        assertThat(media.getOriginFile(), is(sut));
    }

}
