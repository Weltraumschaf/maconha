package de.weltraumschaf.maconha.job;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Tests for {@link JobMessage}.
 */
public class JobMessageTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(JobMessage.class).verify();
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_producerNameMustNotBeNull() {
        new JobMessage(null, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_producerNameMustNotBeEmpty() {
        new JobMessage("", "foo");
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_bodyMustNotBeNull() {
        new JobMessage("foo", null);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_bodyNameMustNotBeEmpty() {
        new JobMessage("foo", "");
    }
}
