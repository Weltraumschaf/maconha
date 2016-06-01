
package de.weltraumschaf.maconha.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link Keyword}.
 */
public class KeywordTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(Keyword.class).verify();
    }

}
