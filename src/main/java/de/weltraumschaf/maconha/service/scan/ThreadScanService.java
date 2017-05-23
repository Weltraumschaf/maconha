package de.weltraumschaf.maconha.service.scan;

import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.service.ScanService;
import de.weltraumschaf.maconha.service.ScanServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
final class ThreadScanService implements ScanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadScanService.class);

    @PostConstruct
    public void init() {
        LOGGER.debug("Initialize thread based scan service.");
    }

    @Override
    public Long scan(final Bucket bucket, final UI currentUi) {
        LOGGER.debug("FOOOOOOOOO!");
        return 0L;
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
