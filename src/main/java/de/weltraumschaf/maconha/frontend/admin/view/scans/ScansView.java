package de.weltraumschaf.maconha.frontend.admin.view.scans;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import de.weltraumschaf.maconha.frontend.admin.view.SubView;
import de.weltraumschaf.maconha.service.ScanService;
import de.weltraumschaf.maconha.service.ScanService.ScanStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * This view shows an {@link ScanStatus status} overview of all jobs started.
 */
@UIScope
@SpringComponent
@SpringView(name = ScansView.VIEW_NAME)
public final class ScansView extends SubView {
    public static final String VIEW_NAME = "scans";
    public static final String TITLE = "Scans";

    private static final Logger LOGGER = LoggerFactory.getLogger(ScansView.class);
    private static final String TITLE_ID = "Scans-title";

    private final MGrid<ScanStatus> list = new MGrid<>(ScanStatus.class)
        .withProperties("id", "startTime", "elapsedTime", "statusCode", "exitDescription")
        .withColumnHeaders("ID", "Started", "Elapsed Time", "Status Code", "Exit Description")
        .withFullWidth();
    private final ScanService scanner;

    @Autowired
    ScansView(final ScanService scanner) {
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
            list
        ).expand(list);
        listEntities();
        return content;
    }

    private void listEntities() {
        list.setRows(scanner.overview());
    }
}
