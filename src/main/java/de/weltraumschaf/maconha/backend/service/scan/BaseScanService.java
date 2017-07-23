package de.weltraumschaf.maconha.backend.service.scan;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.service.ScanService;
import de.weltraumschaf.maconha.backend.service.ScanStatusService;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Common functionality for scan services.
 */
abstract class BaseScanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseScanService.class);

    final Map<Long, Execution> scans = new ConcurrentHashMap<>();
    final ScanStatusService statuses;
    private final PeriodFormatter secondsFormat =
        new PeriodFormatterBuilder()
            .printZeroAlways()
            .minimumPrintedDigits(2)
            .appendHours()
            .appendSeparator(":")
            .appendMinutes()
            .appendSeparator(":")
            .appendSeconds()
            .toFormatter();
    private final DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("HH:mm:ss dd.MM.yyyy");

    BaseScanService(final ScanStatusService statuses) {
        this.statuses = statuses;
    }

    final Execution getExecution(final Long id) {
        if (scans.containsKey(id)) {
            return scans.get(id);
        }

        throw new ScanService.ScanError("There's no such job with id %d!", id);
    }

    final String formatDuration(final DateTime startTime, final DateTime endTime ) {
        Validate.notNull(startTime, "startTime");
        Validate.notNull(endTime, "endTime");
        return secondsFormat.print(new Duration(startTime, endTime).toPeriod());
    }

    final String formatDateTime(final Date date) {
        return formatDateTime(new DateTime(date));
    }

    final String formatDateTime(final DateTime dateTime) {
        Validate.notNull(dateTime, "dateTime");
        return dateTimeFormat.print(dateTime);
    }
}
