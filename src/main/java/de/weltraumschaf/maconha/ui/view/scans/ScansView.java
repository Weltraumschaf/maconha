package de.weltraumschaf.maconha.ui.view.scans;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import de.weltraumschaf.maconha.app.MaconhaConfiguration;
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

    private final Button stop = new MButton(VaadinIcons.STOP_COG, this::stop);
    private final MGrid<ScanStatus> list = new MGrid<>(ScanStatus.class)
        .withProperties("id", "bucketName", "creationTime", "startTime", "endTime", "duration", "jobStatus")
        .withColumnHeaders("ID", "Bucket Name", "Created", "Started", "Finished", "Duration", "Status")
        .withFullWidth();
    private final transient ScanService scanner;

    @Autowired
    ScansView(final ScanService scanner, final MaconhaConfiguration config) {
        super(TITLE, TITLE_ID);
        this.scanner = scanner;
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
            new MHorizontalLayout(stop),
            list
        ).expand(list);

        listEntities();
        list.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());

        return content;
    }

    private void listEntities() {
        list.setRows(scanner.overview());
        adjustActionButtonState();
    }

    private void adjustActionButtonState() {
        boolean hasSelection = !list.getSelectedItems().isEmpty();
        stop.setEnabled(hasSelection);
    }

    public void stop(final Button.ClickEvent event) {
        final ScanStatus status = list.asSingleSelect().getValue();
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
}
