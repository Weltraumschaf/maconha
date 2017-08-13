package de.weltraumschaf.maconha.backend.service.scan.eventloop;

import org.junit.Test;
import org.mockito.InOrder;

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

    @Test(expected = NullPointerException.class)
    public void register_handlerMustNotBeNull() {
        sut.register(EventType.LOAD_FILE_HASHES, null);
    }

    @Test
    public void start_invokesRegisteredHandlers() {
        final Event firstEvent = new Event(EventType.DIR_HASH, "firstEvent");
        final Event secondEvent = new Event(EventType.FILTER_SEEN_HASHED_FILE, "secondEvent");
        final Event thirdEvent = new Event(EventType.LOAD_FILE_HASHES, "thirdEvent");

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

        sut.register(EventType.DIR_HASH, handlerOne);
        sut.register(EventType.FILTER_SEEN_HASHED_FILE, handlerTwo);
        sut.register(EventType.LOAD_FILE_HASHES, handlerThree);

        sut.start(firstEvent);

        final InOrder ordered = inOrder(handlerOne, handlerTwo, handlerThree);
        ordered.verify(handlerOne, times(1)).process(any(EventContext.class), eq(firstEvent));
        ordered.verify(handlerTwo, times(1)).process(any(EventContext.class), eq(secondEvent));
        ordered.verify(handlerThree, times(1)).process(any(EventContext.class), eq(thirdEvent));
    }
}
