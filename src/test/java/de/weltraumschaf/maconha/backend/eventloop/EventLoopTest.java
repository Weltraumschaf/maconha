package de.weltraumschaf.maconha.backend.eventloop;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link EventLoop}.
 */
public final class EventLoopTest {
    private final EventLoop sut = new EventLoop();

    @Test(expected = NullPointerException.class)
    public void register_typeMustNotBeNull() {
        sut.register(null, (context, event) -> {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void register_typeMustNotBeEmpty() {
        sut.register("", (context, event) -> {});
    }

    @Test(expected = NullPointerException.class)
    public void register_handlerMustNotBeNull() {
        sut.register("type", null);
    }

    @Test
    public void start_invokesRegisteredHandlers() {
        final Event firstEvent = new Event("handlerOne", "firstEvent");
        final Event secondEvent = new Event("handlerTwo", "secondEvent");
        final Event thirdEvent = new Event("handlerThree", "thirdEvent");

        final EventHandler handlerOne = mock(EventHandler.class);
        doAnswer(invocation -> {
            final EventContext ctx = invocation.getArgument(0);
            ctx.emitter().emmit(secondEvent);
            return null;
        }).when(handlerOne).process(any(EventContext.class), any(Event.class));

        final EventHandler handlerTwo = mock(EventHandler.class);
        doAnswer(invocation -> {
            final EventContext ctx = invocation.getArgument(0);
            ctx.emitter().emmit(thirdEvent);
            return null;
        }).when(handlerTwo).process(any(EventContext.class), any(Event.class));

        final EventHandler handlerThree = mock(EventHandler.class);

        sut.register("handlerOne", handlerOne);
        sut.register("handlerTwo", handlerTwo);
        sut.register("handlerThree", handlerThree);

        sut.start(firstEvent);

        final InOrder ordered = inOrder(handlerOne, handlerTwo, handlerThree);
        ordered.verify(handlerOne, times(1)).process(any(EventContext.class), eq(firstEvent));
        ordered.verify(handlerTwo, times(1)).process(any(EventContext.class), eq(secondEvent));
        ordered.verify(handlerThree, times(1)).process(any(EventContext.class), eq(thirdEvent));
    }
}
