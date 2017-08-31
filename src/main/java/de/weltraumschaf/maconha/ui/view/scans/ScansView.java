package de.weltraumschaf.maconha.ui.view.scans;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.weltraumschaf.maconha.backend.service.ScanReportService;
import de.weltraumschaf.maconha.backend.service.ScanService;
import de.weltraumschaf.maconha.backend.service.ScanService.ScanStatus;
import de.weltraumschaf.maconha.ui.helper.Expander;
import de.weltraumschaf.maconha.ui.view.SubView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

    private final Button stop = new Button("Stop", VaadinIcons.STOP_COG);
    private final Button report = new Button("Report", VaadinIcons.LIST);
    private final Grid<ScanStatus> statusesList = new Grid<>(ScanStatus.class);
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
        stop.addClickListener(this::stop);
        report.addClickListener(this::report);

        statusesList.setColumns("id", "bucketName", "creationTime", "startTime", "endTime", "duration", "jobStatus");
        statusesList.getColumn("id").setCaption("ID");
        statusesList.getColumn("bucketName").setCaption("Bucket Name");
        statusesList.getColumn("creationTime").setCaption("Created");
        statusesList.getColumn("startTime").setCaption("Started");
        statusesList.getColumn("endTime").setCaption("Finished");
        statusesList.getColumn("duration").setCaption("Duration");
        statusesList.getColumn("jobStatus").setCaption("Status");
        statusesList.setSizeFull();

        final VerticalLayout content = new VerticalLayout(
            new HorizontalLayout(stop, report)
        );
        Expander.addAndExpand(content, statusesList);

        listEntities();
        statusesList.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());

        return content;
    }

    private void listEntities() {
        statusesList.setItems(scanner.overview());
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
