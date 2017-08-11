package de.weltraumschaf.maconha.backend.service.scan.eventloop;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DefaultQueue}.
 */
public final class DefaultQueueTest {
    private final DefaultQueue sut = new DefaultQueue();

    @Test(expected = NullPointerException.class)
    public void emit_doesNotAllowNull() {
        sut.emmit(null);
    }

    @Test
    public void emit() {
        assertThat(sut.size(), is(0));

        sut.emmit(new Event(EventType.LOAD_FILE_HASHES, new Object()));
        assertThat(sut.size(), is(1));

        sut.emmit(new Event(EventType.LOAD_FILE_HASHES, new Object()));
        assertThat(sut.size(), is(2));

        sut.emmit(new Event(EventType.LOAD_FILE_HASHES, new Object()));
        assertThat(sut.size(), is(3));
    }

    @Test
    public void nex() {
        final Event one = new Event(EventType.LOAD_FILE_HASHES, new Object());
        sut.emmit(one);
        final Event two = new Event(EventType.LOAD_FILE_HASHES, new Object());
        sut.emmit(two);
        final Event three = new Event(EventType.LOAD_FILE_HASHES, new Object());
        sut.emmit(three);

        assertThat(sut.next(), is(one));
        assertThat(sut.next(), is(two));
        assertThat(sut.next(), is(three));
        assertThat(sut.next(), is(nullValue()));
        assertThat(sut.size(), is(0));
    }

    @Test
    public void isEmpty() {
        assertThat(sut.isEmpty(), is(true));

        sut.emmit(new Event(EventType.LOAD_FILE_HASHES, new Object()));
        assertThat(sut.isEmpty(), is(false));

        sut.next();
        assertThat(sut.isEmpty(), is(true));
    }
}
