package de.weltraumschaf.maconha.service.scan;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common functionality for scan services.
 */
abstract class BaseScanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseScanService.class);

    final PeriodFormatter secondsFormat =
        new PeriodFormatterBuilder()
            .printZeroAlways()
            .minimumPrintedDigits(2)
            .appendHours()
            .appendSeparator(":")
            .appendMinutes()
            .appendSeparator(":")
            .appendSeconds()
            .toFormatter();

    Notification notification(final String caption, final String description, final Object... args) {
        return new Notification(caption, String.format(description, args), Notification.Type.TRAY_NOTIFICATION);
    }

    final void notifyClient(final Long jobId, final Notification notification, final UI ui) {
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
