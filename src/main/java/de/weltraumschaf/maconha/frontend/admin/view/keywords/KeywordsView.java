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
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
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
    private MTextField filterByLiteral = new MTextField()
        .withPlaceholder("Filter by literal");
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
            new MHorizontalLayout(filterByLiteral),
            list
        ).expand(list);
        listEntities();
        filterByLiteral.addValueChangeListener(e -> listEntities(e.getValue()));
        return content;
    }

    private void listEntities() {
        listEntities(filterByLiteral.getValue());
    }

    private void listEntities(final String literalFilter) {
        final String normalizedFilter = literalFilter.trim().toLowerCase();

        if (normalizedFilter.isEmpty()) {
            LOGGER.debug("List all keyword entities.");
            list.setRows(keywords.findAll());
        } else {
            final String likeFilter = "%" + normalizedFilter + "%";
            LOGGER.debug("List all keyword entities like {}.", likeFilter);
            list.setRows(keywords.findByLiteralLike(likeFilter));
        }
    }
}
