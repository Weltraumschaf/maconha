package de.weltraumschaf.maconha.ui.view.scans;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.Report;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.ReportEntry;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;

/**
 *
 */
@UIScope
@SpringComponent
final class ReportView extends CustomComponent {

    private final MGrid<ReportEntry> list = new MGrid<>(ReportEntry.class)
        .withProperties("type", "source", "message")
        .withColumnHeaders("Type", "Source", "Message")
        .withFullWidth();
    private String modalWindowTitle = "Report";
    private Report report;

    @PostConstruct
    public void init() {
        setCompositionRoot(buildContent());
    }

    void setModalWindowTitle(String modalWindowTitle) {
        this.modalWindowTitle = modalWindowTitle;
    }

    void openInModalPopup() {
        final Window popup = new Window(modalWindowTitle, this);
        popup.setModal(true);
        listEntities();
        UI.getCurrent().addWindow(popup);
    }

    private Component buildContent() {
        return new MVerticalLayout(
            list
        );
    }

    private void listEntities() {
        list.setRows(report.entries());
    }

    void setReport(final Report report) {
        this.report = Validate.notNull(report, "report");
    }
}
