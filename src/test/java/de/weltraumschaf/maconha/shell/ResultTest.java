package de.weltraumschaf.maconha.shell;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link de.weltraumschaf.maconha.shell.Result}.
 */
public class ResultTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(de.weltraumschaf.maconha.shell.Result.class).verify();
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_stdoutMustNotBeNull() {
        new de.weltraumschaf.maconha.shell.Result(0, null, "");
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_stderrMustNotBeNull() {
        new de.weltraumschaf.maconha.shell.Result(0, "", null);
    }

    @Test
    public void isSuccessful() {
        assertThat(new de.weltraumschaf.maconha.shell.Result(0, "", "").isSuccessful(), is(true));
        assertThat(new de.weltraumschaf.maconha.shell.Result(-1, "", "").isSuccessful(), is(false));
        assertThat(new de.weltraumschaf.maconha.shell.Result(1, "", "").isSuccessful(), is(false));
    }

    @Test
    public void isFailed() {
        assertThat(new de.weltraumschaf.maconha.shell.Result(0, "", "").isFailed(), is(false));
        assertThat(new de.weltraumschaf.maconha.shell.Result(-1, "", "").isFailed(), is(true));
        assertThat(new Result(1, "", "").isFailed(), is(true));
    }
}
