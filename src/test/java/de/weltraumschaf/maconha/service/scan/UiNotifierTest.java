package de.weltraumschaf.maconha.service.scan;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link UiNotifier}.
 */
public final class UiNotifierTest {

    @Test
    public void notification_withoutFormat() {
        final Notification notification = UiNotifier.notification("caption", "foo bar 23!");

        assertThat(notification.getCaption(), is("caption"));
        assertThat(notification.getDescription(), is("foo bar 23!"));
    }

    @Test
    public void notification_withFormat() {
        final Notification notification = UiNotifier.notification("caption", "%s bar %d!", "foo", 23);

        assertThat(notification.getCaption(), is("caption"));
        assertThat(notification.getDescription(), is("foo bar 23!"));
    }

    @Test
    public void notifyClient_uiIsNull() {
        final Notification notification = mock(Notification.class);

        UiNotifier.notifyClient(42L, notification, null);

        verify(notification, never()).show(any(Page.class));
    }

    @Test
    public void notifyClient_pageIsNull() {
        final Notification notification = mock(Notification.class);
        final UI ui = mock(UI.class);
        when(ui.access(any())).then(invocation -> {
            final Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        });

        UiNotifier.notifyClient(42L, notification, ui);

        verify(notification, never()).show(any(Page.class));
    }

    @Test
    public void notifyClient() {
        final Notification notification = mock(Notification.class);
        final UI ui = mock(UI.class);
        when(ui.access(any())).then(invocation -> {
            final Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        });
        final Page page = mock(Page.class);
        when(ui.getPage()).thenReturn(page);

        UiNotifier.notifyClient(42L, notification, ui);

        verify(notification, times(1)).show(page);
    }

    @Test
    public void notifyClient_throwsUIDetachedException() {
        final Notification notification = mock(Notification.class);
        final Page page = mock(Page.class);
        doThrow(UIDetachedException.class).when(notification).show(page);
        final UI ui = mock(UI.class);
        when(ui.access(any())).then(invocation -> {
            final Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        });
        when(ui.getPage()).thenReturn(page);

        UiNotifier.notifyClient(42L, notification, ui);

        verify(notification, times(1)).show(page);
    }
}
