package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.job.Job.State;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.joda.time.LocalDateTime;
import org.junit.Test;

/**
 * Tests for {@link JobInfo}.
 */
public class JobInfoTest {

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_nameMustNotBeNull() {
        new JobInfo(null,  State.NEW, 0d, new LocalDateTime());
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_nameMustNotBeEmpty() {
        new JobInfo("", State.NEW, 0d, new LocalDateTime());
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_stateMustNotBeNull() {
        new JobInfo("name", null, 0d, new LocalDateTime());
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_progressMustNotBeNegative() {
        new JobInfo("name", State.NEW, -1d, new LocalDateTime());
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_startTimeMustNotBeNull() {
        new JobInfo("name",  State.NEW, 0d, null);
    }

    @Test
    public void equalsAndHAshCode() {
        EqualsVerifier.forClass(JobInfo.class).verify();
    }
}
