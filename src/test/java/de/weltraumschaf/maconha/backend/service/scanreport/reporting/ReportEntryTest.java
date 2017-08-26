package de.weltraumschaf.maconha.backend.service.scanreport.reporting;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ReportEntry}.
 */
public class ReportEntryTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(ReportEntry.class).verify();
    }

    @Test
    public void construct_typeMustNotBeNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("type");

        new ReportEntry(null, "source", "message");
    }

    @Test
    public void construct_sourceMustNotBeNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("source");

        new ReportEntry(ReportEntryType.NORMAL, null, "message");
    }

    @Test
    public void construct_sourceMustNotBeEmpty() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("source");

        new ReportEntry(ReportEntryType.NORMAL, "", "message");
    }

    @Test
    public void construct_formatMessageMustBotBeNull() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("formatMessage");

        new ReportEntry(ReportEntryType.NORMAL, "source", null);
    }

    @Test
    public void construct_formatMessageMustBotBeEmptyl() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("formatMessage");

        new ReportEntry(ReportEntryType.NORMAL, "source", "");
    }

    @Test
    public void construct_formatMessage() {
        final ReportEntry sut = new ReportEntry(ReportEntryType.NORMAL, "source", "%s%s", "foo", "bar");

        assertThat(sut.getMessage(), is("foobar"));
    }
}