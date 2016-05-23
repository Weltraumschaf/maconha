package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.job.Job.State;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Tests for {@link JobDescription}.
 */
public class JobDescriptionTest {

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_nameMustNotBeNull() {
        new JobDescription(null, Job.class, State.NEW);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_nameMustNotBeEmpty() {
        new JobDescription("", Job.class, State.NEW);
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_typeMustNotBeNull() {
        new JobDescription("name", null, State.NEW);
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_stateMustNotBeNull() {
        new JobDescription("name", Job.class, null);
    }

    @Test
    public void equalsAndHAshCode() {
        EqualsVerifier.forClass(JobDescription.class).verify();
    }
}
