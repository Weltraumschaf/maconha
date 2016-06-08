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
        new JobInfo(null,  State.NEW, 0d);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_nameMustNotBeEmpty() {
        new JobInfo("", State.NEW, 0d);
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_stateMustNotBeNull() {
        new JobInfo("name", null, 0d);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_progressMustNotBeNegative() {
        new JobInfo("name", State.NEW, -1d);
    }

    @Test
    public void equalsAndHAshCode() {
        EqualsVerifier.forClass(JobInfo.class).verify();
    }
}
