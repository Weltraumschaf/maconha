package de.weltraumschaf.maconha.frontend.admin.view.keywords;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import de.weltraumschaf.maconha.frontend.admin.view.SubView;
import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * View to manage {@link Keyword keywords}.
 */
@UIScope
@SpringComponent
@SpringView(name = KeywordsView.VIEW_NAME)
public final class KeywordsView extends SubView {
    public static final String VIEW_NAME = "keywords";
    public static final String TITLE = "Keywords";

    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordsView.class);
    private static final String TITLE_ID = "keywords-title";

    private final MGrid<Keyword> list = new MGrid<>(Keyword.class)
        .withProperties("id", "literal")
        .withColumnHeaders("ID", "Literal")
        .withFullWidth();
    private final KeywordRepo keywords;

    @Autowired
    KeywordsView(final KeywordRepo keywords) {
        super(TITLE, TITLE_ID);
        this.keywords = keywords;
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
        LOGGER.debug("List keyword entities.");
        list.deselectAll();
        list.setRows(keywords.findAll());
    }
}
