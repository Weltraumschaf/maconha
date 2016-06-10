
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
        EqualsVerifier
            .forClass(OriginFile.class)
            .withPrefabValues(Media.class, new Media().setId(1), new Media().setId(2))
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
