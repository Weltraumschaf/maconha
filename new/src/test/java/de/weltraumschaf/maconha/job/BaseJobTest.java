package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.job.Job.State;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link BaseJob}.
 */
public class BaseJobTest {

    private final BaseJob<String> sut = new BaseJobStub("test");

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_nameMustNotBeNull() {
        new BaseJobStub(null);
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void constuct_nameMustNotBeEmpty() {
        new BaseJobStub("");
    }

    @Test
    public void emmit() {
        final MessageConsumer one = mock(MessageConsumer.class);
        sut.register(one);
        final MessageConsumer two = mock(MessageConsumer.class);
        sut.register(two);
        final MessageConsumer three = mock(MessageConsumer.class);
        sut.register(three);

        sut.emmit("hello %s", "world");

        verify(one, times(1)).receive("hello world");
        verify(two, times(1)).receive("hello world");
        verify(three, times(1)).receive("hello world");
    }

    @Test
    public void describe() {
        assertThat(sut.describe(), is(new JobDescription("test", BaseJobStub.class, State.NEW)));
    }

    @Test
    public void defaultState() {
        assertThat(sut.getState(), is(State.NEW));
        assertThat(sut.isRunning(), is(false));
        assertThat(sut.isCanceled(), is(false));
        assertThat(sut.isFinished(), is(false));
    }

    @Test
    public void call() throws Exception {
        assertThat(sut.call(), is("snafu"));
        assertThat(sut.getState(), is(State.FINISHED));
        assertThat(sut.isFinished(), is(true));
    }

    @Test(expected = IllegalStateException.class)
    public void call_finished() throws Exception {
        sut.call();
        sut.call();
    }

    @Test
    public void call_canceled() throws Exception {
        sut.cancel();

        assertThat(sut.call(), is(nullValue()));
    }

    private static final class BaseJobStub extends BaseJob<String> {

        public BaseJobStub(String name) {
            super(name);
        }

        @Override
        protected String execute() throws Exception {
            return "snafu";
        }

    }
}
