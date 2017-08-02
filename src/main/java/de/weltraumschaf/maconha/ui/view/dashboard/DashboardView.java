package de.weltraumschaf.maconha.ui.view.dashboard;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import de.weltraumschaf.maconha.ui.view.SubView;
import de.weltraumschaf.maconha.backend.repo.KeywordRepo;
import de.weltraumschaf.maconha.backend.repo.MediaFileRepo;
import de.weltraumschaf.maconha.ui.view.user.UserViewDesign;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;

@SpringView
public final class DashboardView implements View {

    private final DashboardViewDesign design = new DashboardViewDesign();

    private final MediaFileRepo files;
    private final KeywordRepo keywords;

    @Autowired
    public DashboardView(final MediaFileRepo files, final KeywordRepo keywords) {
        super();
        this.files = files;
        this.keywords = keywords;
    }

    protected void subInit() {
//        content.addComponent(new Label("Top 10 keywords:"));
//        final MVerticalLayout topTenKeywords = new MVerticalLayout(
//            new Label("keyword1"),
//            new Label("keyword2"),
//            new Label("keyword3"),
//            new Label("keyword4"),
//            new Label("keyword5"),
//            new Label("keyword6"),
//            new Label("keyword7"),
//            new Label("keyword8"),
//            new Label("keyword9"),
//            new Label("keyword10"));
//        content.addComponent(topTenKeywords);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        final NumberFormat formatter = NumberFormat.getInstance();
        design.numberOfIndexedFiles.setValue(formatter.format(files.count()));
        design.numberOfDuplicteFiles.setValue(formatter.format(files.countDuplicates()));
        design.numberOfFoundKeywords.setValue(formatter.format(keywords.count()));
    }

    @Override
    public DashboardViewDesign getViewComponent() {
        return design;
    }
}
