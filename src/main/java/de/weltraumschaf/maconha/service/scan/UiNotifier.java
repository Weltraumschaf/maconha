package de.weltraumschaf.maconha.service.scan;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
final class UiNotifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(UiNotifier.class);

    private UiNotifier() {
        super();
    }

    static Notification notification(final String caption, final String description, final Object... args) {
        return new Notification(caption, String.format(description, args), Notification.Type.TRAY_NOTIFICATION);
    }

    static void notifyClient(final Long jobId, final Notification notification, final UI ui) {
        if (ui == null) {
            LOGGER.warn("Currents UI null! Can't notify client about job with id {}.", jobId);
        } else {
            ui.access(() -> {
                final Page page = ui.getPage();

                if (page == null) {
                    LOGGER.warn("Currents page null! Can't notify client about job with id {}.", jobId);
                } else {
                    notification.show(page);
                }
            });
        }
    }
}
