package de.weltraumschaf.maconha.backend.eventloop;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Tests for {@link Event}.
 */
public final class EventTest {
    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(Event.class).verify();
    }
}
