package de.weltraumschaf.maconha.ui.view.scans;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.Report;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.ReportEntry;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;

/**
 * Shows the report of a scan.
 */
@UIScope
@SpringComponent
final class ReportView extends CustomComponent {

    private final Grid<ReportEntry> list = new Grid<>(ReportEntry.class);
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
        list.setColumns("type", "source", "message");
        list.getColumn("type").setCaption("Type");
        list.getColumn("source").setCaption("Source");
        list.getColumn("message").setCaption("Message");
        list.setWidth("100%");
        return new MVerticalLayout(
            list
        );
    }

    private void listEntities() {
        list.setItems(report.entries());
    }

    void setReport(final Report report) {
        this.report = Validate.notNull(report, "report");
    }
}
