package de.weltraumschaf.maconha.ui.view.dashboard;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import de.weltraumschaf.maconha.backend.service.KeywordService;
import de.weltraumschaf.maconha.backend.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;

@SpringView
public final class DashboardView implements View {

    private final DashboardViewDesign design = new DashboardViewDesign();

    private final MediaFileService files;
    private final KeywordService keywords;

    @Autowired
    public DashboardView(final MediaFileService files, final KeywordService keywords) {
        super();
        this.files = files;
        this.keywords = keywords;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        final NumberFormat formatter = NumberFormat.getInstance();
        design.numberOfIndexedFiles.setValue(formatter.format(files.numberOfIndexedFiles()));
        design.numberOfDuplicteFiles.setValue(formatter.format(files.numberOfDuplicateFiles()));
        design.numberOfFoundKeywords.setValue(formatter.format(keywords.numberOfKeywords()));
        design.topTenKeywords.setColumns("literal", "numberOfMediaFiles");
        design.topTenKeywords.removeHeaderRow(0);
        design.topTenKeywords.setItems(keywords.topTen());
    }

    @Override
    public DashboardViewDesign getViewComponent() {
        return design;
    }
}
