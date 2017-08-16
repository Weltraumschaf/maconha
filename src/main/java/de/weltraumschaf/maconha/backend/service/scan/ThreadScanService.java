package de.weltraumschaf.maconha.backend.service.scan;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.MaconhaConfiguration;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.service.MediaFileService;
import de.weltraumschaf.maconha.backend.service.ScanService;
import de.weltraumschaf.maconha.backend.service.ScanStatusService;
import de.weltraumschaf.maconha.backend.service.scan.shell.Command;
import de.weltraumschaf.maconha.backend.service.scan.shell.CommandFactory;
import de.weltraumschaf.maconha.backend.service.scan.shell.Commands;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread executor based implementation.
 * <p>
 * This class is package private because it must not be used directly from outside. Use the DI of Spring to get an
 * instance.
 * </p>
 * <ul>
 * <li><a href="http://www.deadcoderising.com/java8-writing-asynchronous-code-with-completablefuture/">Writing asynchronous code with CompletableFuture</a></li>
 * <li><a href="https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletionStage.html">Interface CompletionStage&lt;T&gt;</a></li>
 * </ul>
 */
@Service
final class ThreadScanService  implements ScanService, ScanCallBack {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadScanService.class);

    // FIXME Make private.
    final Map<Long, Execution> scans = new ConcurrentHashMap<>();
    private final ScanStatusService statuses;
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

    private final MaconhaConfiguration config;
    private final MediaFileService mediaFiles;
    private final TaskExecutor executor;
    private CommandFactory cmds;

    @Autowired
    ThreadScanService(final MaconhaConfiguration config, final MediaFileService mediaFiles, final TaskExecutor executor, final ScanStatusService statuses) {
        super();
        this.statuses = statuses;
        this.config = config;
        this.mediaFiles = mediaFiles;
        this.executor = executor;
    }

    @PostConstruct
    public void initHook() {
        LOGGER.debug("Initialize thread based scan service.");
        cmds = new Commands(Paths.get(config.getBindir()));
    }

    @Override
    public void scan(final Bucket bucket, final UI currentUi) {
        final long id = statuses.nextId();
        final Command dirhash = cmds.dirhash(Paths.get(bucket.getDirectory()));
        final ScanTask task = new DefaultScanTask(id, bucket, currentUi, dirhash, mediaFiles, this);
        final Execution execution = new Execution(id, bucket, currentUi, task);
        executor.execute(task);
        scans.put(id, execution);
    }

    @Override
    public boolean stop(final long executionId) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public List<ScanStatus> overview() {
        final List<ScanStatus> allScans = scans.values()
            .stream()
            .map(this::convert)
            .collect(Collectors.toList());
        allScans.addAll(statuses.allStatuses());
        return allScans;
    }

    @Override
    public void beforeScan(final long id) {
        final Execution execution = getExecution(id);
        execution.start();
        Notification notification = UiNotifier.notification(
            "Scan job started",
            "Scan for bucket '%s' in directory '%s' with id %d started.",
            execution.getBucket().getName(), execution.getBucket().getDirectory(), id);
        UiNotifier.notifyClient(id, notification, execution.getCurrentUi());
    }

    @Override
    public void afterScan(final long id) {
        final Execution execution = getExecution(id);
        execution.stop();
        final String duration = formatDuration(execution.getStartTime(), execution.getStopTime());

        final Notification notification = UiNotifier.notification(
            "Scan job finished",
            "Scan for bucket '%s' in directory '%s' with id %d finished in %s.",
            execution.getBucket().getName(), execution.getBucket().getDirectory(), id, duration);
        UiNotifier.notifyClient(id, notification, execution.getCurrentUi());

        statuses.storeStatus(convert(execution));
        scans.remove(id);
    }

    private ScanStatus convert(final Execution execution) {
        final DateTime startTime = execution.getStartTime();
        final DateTime endTime;
        final String formattedEndTime;

        if (execution.hasStopTime()) {
            endTime = execution.getStopTime();
            formattedEndTime = formatDateTime(endTime);
        } else {
            endTime = DateTime.now();
            formattedEndTime = "-";
        }

        final String jobStatus;

        if (execution.hasStopTime()) {
            jobStatus = "COMPLETED";
        } else {
            if (execution.hasStartTime()) {
                jobStatus = "RUNNING";
            } else {
                jobStatus = "CREATED";
            }
        }

        return new ScanStatus(
            execution.getId(),
            execution.getBucket().getName(),
            formatDateTime(execution.getCreationTime()),
            formatDateTime(execution.getStartTime()),
            formattedEndTime,
            formatDuration(startTime, endTime),
            jobStatus);
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
