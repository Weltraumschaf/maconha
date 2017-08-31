package de.weltraumschaf.maconha.ui.view.keywords;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.Keyword;
import de.weltraumschaf.maconha.backend.model.entity.MediaFile;
import de.weltraumschaf.maconha.backend.repo.KeywordRepo;
import de.weltraumschaf.maconha.backend.repo.MediaFileRepo;
import de.weltraumschaf.maconha.ui.helper.Expander;
import de.weltraumschaf.maconha.ui.view.SubView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * View to manage {@link Keyword keywords}.
 */
@UIScope
@SpringComponent
@SpringView(name = KeywordsView.VIEW_NAME)
public final class KeywordsView extends SubView implements HasLogger {
    public static final String TITLE = "Keywords";

    static final String VIEW_NAME = "keywords";
    private static final String TITLE_ID = "keywords-title";
    private static final String TOTAL_NUMBER_OF_FOUND_KEYWORDS = "Total number of found keywords: %d";

    private final Label totalNumber = new Label(String.format(TOTAL_NUMBER_OF_FOUND_KEYWORDS, 0));
    private final Button showFiles = new Button("Show files", VaadinIcons.BULLETS);
    private final TextField filterByLiteral = new TextField();
    private final Grid<Keyword> list = new Grid<>(Keyword.class);

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
        showFiles.addClickListener(this::showFiles);
        filterByLiteral.setPlaceholder("Filter by literal");

        final VerticalLayout content = new VerticalLayout(
            new HorizontalLayout(filterByLiteral, showFiles),
            totalNumber
        );

        list.setColumns("id", "literal");
        list.getColumn("id").setCaption("ID");
        list.getColumn("literal").setCaption("Literal");
        list.setSizeFull();
        Expander.addAndExpand(content, list);
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
            logger().debug("List all keyword entities.");
             found= keywords.findAll();
        } else {
            final String likeFilter = "%" + normalizedFilter + "%";
            logger().debug("List all keyword entities like {}.", likeFilter);
            found = keywords.findByLiteralLike(likeFilter);
        }

        totalNumber.setValue(String.format(TOTAL_NUMBER_OF_FOUND_KEYWORDS, found.size()));
        list.setItems(found);
        adjustActionButtonState();
    }

    private void adjustActionButtonState() {
        boolean hasSelection = !list.getSelectedItems().isEmpty();
        showFiles.setEnabled(hasSelection);
    }

    private void showFiles(final Button.ClickEvent event) {
        final Grid<MediaFile> fileList = new Grid<>(MediaFile.class);
        fileList.setColumns("id", "type", "format", "relativeFileName");
        fileList.getColumn("id").setCaption("ID");
        fileList.getColumn("type").setCaption("Type");
        fileList.getColumn("format").setCaption("Format");
        fileList.getColumn("relativeFileName").setCaption("relative File Name");
        fileList.setSizeFull();

        final Keyword keyword = list.asSingleSelect().getValue();
        final List<MediaFile> files = mediaFiles.findByKeywords(keyword);
        fileList.setItems(files);

        final VerticalLayout content = new VerticalLayout(
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
