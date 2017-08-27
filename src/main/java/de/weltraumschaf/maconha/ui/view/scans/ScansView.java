package de.weltraumschaf.maconha.ui.view.scans;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import de.weltraumschaf.maconha.backend.service.ScanReportService;
import de.weltraumschaf.maconha.ui.view.SubView;
import de.weltraumschaf.maconha.backend.service.ScanService;
import de.weltraumschaf.maconha.backend.service.ScanService.ScanStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * This view shows an {@link ScanStatus status} overview of all jobs started.
 */
@UIScope
@SpringComponent
@SpringView(name = ScansView.VIEW_NAME)
public final class ScansView extends SubView {
    static final String VIEW_NAME = "scans";
    public static final String TITLE = "Scans";

    private static final Logger LOGGER = LoggerFactory.getLogger(ScansView.class);
    private static final String TITLE_ID = "Scans-title";

    private final Button stop = new MButton(VaadinIcons.STOP_COG, "Stop", this::stop);
    private final Button report = new MButton(VaadinIcons.LIST, "Report", this::report);
    private final MGrid<ScanStatus> statusesList = new MGrid<>(ScanStatus.class)
        .withProperties("id", "bucketName", "creationTime", "startTime", "endTime", "duration", "jobStatus")
        .withColumnHeaders("ID", "Bucket Name", "Created", "Started", "Finished", "Duration", "Status")
        .withFullWidth();
    private final ReportView reportDialog;
    private final transient ScanService scanner;
    private final transient ScanReportService reports;

    @Autowired
    ScansView(final ScanService scanner, final ScanReportService reports, final ReportView reportDialog) {
        super(TITLE, TITLE_ID);
        this.scanner = scanner;
        this.reports = reports;
        this.reportDialog = reportDialog;
    }

    @Override
    protected void subInit() {
        root.addComponent(buildContent());
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        listEntities();
    }

    private Component buildContent() {
        final MVerticalLayout content = new MVerticalLayout(
            new MHorizontalLayout(stop, report),
            statusesList
        ).expand(statusesList);

        listEntities();
        statusesList.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());

        return content;
    }

    private void listEntities() {
        statusesList.setRows(scanner.overview());
        adjustActionButtonState();
    }

    private void adjustActionButtonState() {
        boolean hasSelection = !statusesList.getSelectedItems().isEmpty();
        stop.setEnabled(hasSelection);
        report.setEnabled(hasSelection);
    }

    public void stop(final Button.ClickEvent event) {
        final ScanStatus status = statusesList.asSingleSelect().getValue();
        LOGGER.debug("Stop scan job with id {}.", status.getId());

        try {
            scanner.stop(status.getId());
            Notification.show(
                "Scan stopped",
                String.format("Scan with id %d stopped.", status.getId()),
                Notification.Type.TRAY_NOTIFICATION);
        } catch (final ScanService.ScanError e) {
            LOGGER.error(e.getMessage(), e);
            Notification.show("Stop failed", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }

        listEntities();
    }

    public void report(final Button.ClickEvent event) {
        final ScanStatus status = statusesList.asSingleSelect().getValue();
        reportDialog.setModalWindowTitle("Report for scan with id " + status.getId());
        reportDialog.setReport(reports.load(status));
        reportDialog.openInModalPopup();
    }
}
