
package de.weltraumschaf.maconha.job;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.mockito.InOrder;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

/**
 * Tests for {@link NoOpJob].
 */
public class NoOpJobTest {

    private final NoOpJob sut = new NoOpJob();

    @Test(expected = IllegalArgumentException.class)
    public void setSeconds_mustNotBeLessThanOne() {
        sut.setSeconds(0);
    }

    @Test
    public void execute() throws Exception {
        sut.setSeconds(3);
        final MessageConsumer receiver = mock(MessageConsumer.class);
        sut.register(receiver);

        sut.execute();

        assertThat(sut.monitor().getTotalWork(), is(3));
        assertThat(sut.monitor().progress(), is(1.0));

        final InOrder inOrder = inOrder(receiver);
        inOrder.verify(receiver, times(1)).receive(new JobMessage(sut.info().getName(), "Waiting for 3 seconds..."));
        inOrder.verify(receiver, times(1)).receive(new JobMessage(sut.info().getName(), "1 of 3 secondes done."));
        inOrder.verify(receiver, times(1)).receive(new JobMessage(sut.info().getName(), "2 of 3 secondes done."));
        inOrder.verify(receiver, times(1)).receive(new JobMessage(sut.info().getName(), "3 of 3 secondes done."));
        inOrder.verify(receiver, times(1)).receive(new JobMessage(sut.info().getName(), "Ready, waited 3 seconds."));
    }

}
