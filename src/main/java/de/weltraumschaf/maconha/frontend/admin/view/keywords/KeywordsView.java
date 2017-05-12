package de.weltraumschaf.maconha.frontend.admin.view.keywords;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.weltraumschaf.maconha.frontend.admin.view.SubView;
import de.weltraumschaf.maconha.model.Keyword;
import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.repo.KeywordRepo;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

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
    private static final String TOTAL_NUMBER_OF_FOUND_KEYWORDS = "Total number of found keywords: %d";

    private final Label totalNumber = new Label(String.format(TOTAL_NUMBER_OF_FOUND_KEYWORDS, 0));
    private final Button showFiles = new MButton(VaadinIcons.BULLETS, "Show files", this::showFiles);
    private final MTextField filterByLiteral = new MTextField()
        .withPlaceholder("Filter by literal");
    private final MGrid<Keyword> list = new MGrid<>(Keyword.class)
        .withProperties("id", "literal")
        .withColumnHeaders("ID", "Literal")
        .withFullWidth();

    private final KeywordRepo keywords;
    private final MediaFileRepo mediaFiles;

    @Autowired
    public KeywordsView(final KeywordRepo keywords, final MediaFileRepo mediaFiles) {
        super(TITLE, TITLE_ID);
        this.keywords = keywords;
        this.mediaFiles = mediaFiles;
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
            new MHorizontalLayout(filterByLiteral, showFiles),
            totalNumber,
            list
        ).expand(list);
        listEntities();

        list.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());
        filterByLiteral.addValueChangeListener(e -> listEntities(e.getValue()));

        return content;
    }

    private void listEntities() {
        listEntities(filterByLiteral.getValue());
    }

    private void listEntities(final String literalFilter) {
        final String normalizedFilter = literalFilter.trim().toLowerCase();
        final List<Keyword> found;

        if (normalizedFilter.isEmpty()) {
            LOGGER.debug("List all keyword entities.");
             found= keywords.findAll();
        } else {
            final String likeFilter = "%" + normalizedFilter + "%";
            LOGGER.debug("List all keyword entities like {}.", likeFilter);
            found = keywords.findByLiteralLike(likeFilter);
        }

        totalNumber.setValue(String.format(TOTAL_NUMBER_OF_FOUND_KEYWORDS, found.size()));
        list.setRows(found);
        adjustActionButtonState();
    }

    private void adjustActionButtonState() {
        boolean hasSelection = !list.getSelectedItems().isEmpty();
        showFiles.setEnabled(hasSelection);
    }

    private void showFiles(final Button.ClickEvent event) {
        final MGrid<MediaFile> fileList = new MGrid<>(MediaFile.class)
            .withProperties("id", "type", "format", "relativeFileName")
            .withColumnHeaders("ID", "Type", "Format", "relative File Name")
            .withFullWidth();

        final Keyword keyword = list.asSingleSelect().getValue();
        final List<MediaFile> files = mediaFiles.findByKeywords(keyword);
        fileList.setRows(files);

        final VerticalLayout content = new MVerticalLayout(
            new Label(
                String.format("Keyword '<strong>%s</strong>' found in <strong>%d</strong> files",
                    keyword.getLiteral(), files.size()),
                ContentMode.HTML),
            fileList
        );
        content.setMargin(true);

        final Window window = new Window("Files");
        window.setWidth(70, Unit.PERCENTAGE);
        window.setContent(content);
        window.center();

        getUI().addWindow(window);
    }
}
