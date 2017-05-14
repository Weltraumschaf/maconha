
package de.weltraumschaf.maconha.service.scan.hashing;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Tests for {@link HashedFile}.
 */
public class HashedFileTest {

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_hashMustNotBeNull() {
        new HashedFile(null, "file");
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_hashMustNotBeEmpty() {
        new HashedFile("", "file");
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_fileMustNotBeNull() {
        new HashedFile("hash", null);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_fileMustNotBeEmpty() {
        new HashedFile("hash", "");
    }

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(HashedFile.class).verify();
    }
}
