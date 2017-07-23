package de.weltraumschaf.maconha.ui.view.dashboard;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import de.weltraumschaf.maconha.ui.view.SubView;
import de.weltraumschaf.maconha.backend.repo.KeywordRepo;
import de.weltraumschaf.maconha.backend.repo.MediaFileRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;

@UIScope
@SpringComponent
@SpringView(name = DashboardView.VIEW_NAME)
public final class DashboardView extends SubView {
    public static final String VIEW_NAME = "";
    public static final String TITLE = "Dashboard";
    private static final String TITLE_ID = "dashboard-title";

    private final MediaFileRepo files;
    private final KeywordRepo keywords;

    @Autowired
    public DashboardView(final MediaFileRepo files, final KeywordRepo keywords) {
        super(TITLE, TITLE_ID);
        this.files = files;
        this.keywords = keywords;
    }

    @Override
    protected void subInit() {
        final GridLayout content = new GridLayout();
        content.setColumns(2);
        content.setRows(4);
        content.setMargin(true);
        content.setSpacing(true);

        final NumberFormat nf = NumberFormat.getInstance();
        content.addComponent(new Label("Number of indexed files:"));
        content.addComponent(new Label(nf.format(files.count())));

        content.addComponent(new Label("Number of duplicate files:"));
        content.addComponent(new Label(nf.format(files.countDuplicates())));

        content.addComponent(new Label("Number of found keywords:"));
        content.addComponent(new Label(nf.format(keywords.count())));

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

        root.addComponent(content);
    }

}
