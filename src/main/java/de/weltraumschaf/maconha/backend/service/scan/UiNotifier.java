package de.weltraumschaf.maconha.backend.service.scan;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.UIDetachedException;
import de.weltraumschaf.commons.validate.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper to notify the UI about scan statuses.
 */
final class UiNotifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(UiNotifier.class);

    /**
     * Pure static class.
     */
    private UiNotifier() {
        super();
    }

    /**
     * Factory method to create a notification.
     *
     * @param caption     must not be {@code null}
     * @param description format string, must not be {@code null} nor empty
     * @param args        optional argument for description format string
     * @return never {@code null}
     */
    static Notification notification(final String caption, final String description, final Object... args) {
        return new Notification(caption, String.format(description, args), Notification.Type.TRAY_NOTIFICATION);
    }

    /**
     * Notifies the Vaadin web ui.
     * <p>
     * This method fails silently if the UI can't be notified, but logs warnings.
     * </p>
     *
     * @param jobId must not be {@code null}
     * @param notification must not be {@code null}
     * @param ui may be {@code null}
     */
    static void notifyClient(final Long jobId, final Notification notification, final UI ui) {
        Validate.notNull(jobId, "jobId");
        Validate.notNull(notification, "notification");

        if (ui == null) {
            LOGGER.warn("Currents UI null! Can't notify client about job with id {}.", jobId);
        } else {
            try {
                ui.access(() -> {
                    final Page page = ui.getPage();

                    if (page == null) {
                        LOGGER.warn("Currents page null! Can't notify client about job with id {}.", jobId);
                    } else {
                        notification.show(page);
                    }
                });
            } catch (final UIDetachedException e) {
                LOGGER.warn("The UI was detached!", e);
            }
        }
    }
}
