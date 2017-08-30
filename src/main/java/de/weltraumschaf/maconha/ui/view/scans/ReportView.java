package de.weltraumschaf.maconha.ui.view.scans;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.Report;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.ReportEntry;
import de.weltraumschaf.maconha.ui.helper.Expander;

import javax.annotation.PostConstruct;

/**
 * Shows the report of a scan.
 */
@UIScope
@SpringComponent
final class ReportView extends CustomComponent {

    private final Grid<ReportEntry> reportList = new Grid<>(ReportEntry.class);
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
        popup.setWidth("90%");
        popup.setHeight("90%");
        listEntities();
        UI.getCurrent().addWindow(popup);
    }

    private Component buildContent() {
        reportList.setColumns("type", "source", "message");
        reportList.getColumn("type").setCaption("Type");
        reportList.getColumn("source").setCaption("Source");
        reportList.getColumn("message").setCaption("Message");
        reportList.setSizeFull();

        final VerticalLayout content = new VerticalLayout();
        Expander.addAndExpand(content, reportList);

        return content;
    }

    private void listEntities() {
        reportList.setItems(report.entries());
    }

    void setReport(final Report report) {
        this.report = Validate.notNull(report, "report");
    }
}
