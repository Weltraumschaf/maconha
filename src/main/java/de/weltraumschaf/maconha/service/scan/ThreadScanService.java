package de.weltraumschaf.maconha.service.scan;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.service.ScanService;
import de.weltraumschaf.maconha.service.ScanServiceFactory;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

/**
 * Thread executor based implementation.
 * <p>
 * This class is package private because it must not be used directly from outside. Use the DI of Spring to get an
 * instance.
 * </p>
 */
@Service(ScanServiceFactory.THREAD)
final class ThreadScanService extends BaseScanService implements ScanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadScanService.class);

    @PostConstruct
    public void init() {
        LOGGER.debug("Initialize thread based scan service.");
    }

    @Async
    @Override
    public void scan(final Bucket bucket, final UI currentUi) {
        long id = 0L;
        Notification notification = notification(
            "Scan job started",
            "Scan for bucket '%s' in directory '%s' with id %d started.",
            bucket.getName(), bucket.getDirectory(), id);
        notifyClient(id, notification, currentUi);
        final DateTime startTime = DateTime.now();

        scanImpl();

        final DateTime endTime = DateTime.now();
        final String duration = secondsFormat.print(new Duration(startTime, endTime).toPeriod());
        notification = notification(
            "Scan job finished",
            "Scan for bucket '%s' in directory '%s' with id %d finished in %s.",
            bucket.getName(), bucket.getDirectory(), id, duration);
        notifyClient(id, notification, currentUi);
    }

    private void scanImpl() {
        try {
            Thread.sleep(15_000);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean stop(final long executionId) {
        return false;
    }

    @Override
    public List<ScanStatus> overview() {
        return Collections.emptyList();
    }
}
