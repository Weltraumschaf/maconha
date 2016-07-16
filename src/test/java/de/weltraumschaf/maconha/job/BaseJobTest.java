package de.weltraumschaf.maconha.job;

import de.weltraumschaf.maconha.job.Job.State;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
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

        sut.emit("hello %s", "world");

        verify(one, times(1)).receive(new JobMessage(sut.info().getName(), "hello world"));
        verify(two, times(1)).receive(new JobMessage(sut.info().getName(), "hello world"));
        verify(three, times(1)).receive(new JobMessage(sut.info().getName(), "hello world"));
    }

    @Test
    public void describe() {
        assertThat(sut.info(), is(new JobInfo("test", State.NEW, 0d, sut.getStartTime())));
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

    @Test
    public void foo() throws Exception {
        final Map<String, Object> input = new HashMap<>();
        input.put(RquiredProperty.FOO.getBeanName(), "foo");
        input.put(RquiredProperty.BAR.getBeanName(), true);
        input.put(OptionslProperty.BAZ.getBeanName(), 42);
        final BaseJobStub innerSut = new BaseJobStub("foo");

        innerSut.configure(input);

        assertThat(innerSut.getFoo(), is("foo"));
        assertThat(innerSut.isBar(), is(true));
        assertThat(innerSut.getBaz(), is(42));
    }

    private static final class BaseJobStub extends BaseJob<String> {

        private String foo;
        private boolean bar;
        private int baz;

        public BaseJobStub(String name) {
            super(name);
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }

        public void setBar(boolean bar) {
            this.bar = bar;
        }

        public void setBaz(int baz) {
            this.baz = baz;
        }

        public String getFoo() {
            return foo;
        }

        public boolean isBar() {
            return bar;
        }

        public int getBaz() {
            return baz;
        }

        @Override
        protected String execute() throws Exception {
            return "snafu";
        }

        @Override
        protected Description description() {
            return new Description(BaseJobStub.class, EnumSet.allOf(RquiredProperty.class), EnumSet.allOf(OptionslProperty.class));
        }

    }

    enum RquiredProperty implements Property {
        FOO("foo"), BAR("bar");
        private final String name;

        private RquiredProperty(String name) {
            this.name = name;
        }

        @Override
        public String getBeanName() {
            return name;
        }
    }

    enum OptionslProperty implements Property {
        BAZ("baz");
        private final String name;

        private OptionslProperty(String name) {
            this.name = name;
        }

        @Override
        public String getBeanName() {
            return name;
        }
    }
}
