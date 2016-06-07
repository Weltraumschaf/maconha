package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.job.Job.State;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

/**
 * Tests for {@link JobInfo}.
 */
public class JobInfoTest {

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_nameMustNotBeNull() {
        new JobInfo(null,  State.NEW);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_nameMustNotBeEmpty() {
        new JobInfo("", State.NEW);
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_stateMustNotBeNull() {
        new JobInfo("name", null);
    }

    @Test
    public void equalsAndHAshCode() {
        EqualsVerifier.forClass(JobInfo.class).verify();
    }
}
