package de.weltraumschaf.maconha.shell;

import nl.jqno.equalsverifier.EqualsVerifier;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link Result}.
 */
public class ResultTest {

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(Result.class).verify();
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_stdoutMustNotBeNull() {
        new Result(0, null, "");
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constructor_stderrMustNotBeNull() {
        new Result(0, "", null);
    }

    @Test
    public void isSuccessful() {
        assertThat(new Result(0, "", "").isSuccessful(), is(true));
        assertThat(new Result(-1, "", "").isSuccessful(), is(false));
        assertThat(new Result(1, "", "").isSuccessful(), is(false));
    }

    @Test
    public void isFailed() {
        assertThat(new Result(0, "", "").isFailed(), is(false));
        assertThat(new Result(-1, "", "").isFailed(), is(true));
        assertThat(new Result(1, "", "").isFailed(), is(true));
    }
}
