package de.weltraumschaf.maconha.ui.view.duplicates;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.MediaFile;
import de.weltraumschaf.maconha.backend.repo.MediaFileRepo;
import de.weltraumschaf.maconha.ui.helper.Expander;
import de.weltraumschaf.maconha.ui.view.SubView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * View to manage duplicate media files.
 */
@UIScope
@SpringComponent
@SpringView(name = DuplicatesView.VIEW_NAME)
public final class DuplicatesView extends SubView implements HasLogger {
    public static final String TITLE = "Duplicates";
    static final String VIEW_NAME = "duplicates";

    private static final String TITLE_ID = "duplicates-title";
    private static final String TOTAL_NUMBER_OF_FOUND_DUPLICATES = "Total number of found duplicates: %d";

    private final Label totalNumber = new Label(String.format(TOTAL_NUMBER_OF_FOUND_DUPLICATES, 0));
    private final Grid<MediaFile> list = new Grid<>(MediaFile.class);
    private final MediaFileRepo mediaFiles;

    @Autowired
    public DuplicatesView(final MediaFileRepo mediaFiles) {
        super(TITLE, TITLE_ID);
        this.mediaFiles = mediaFiles;
    }

    @Override
    protected void subInit() {
        root.addComponent(buildContent());
    }

    private Component buildContent() {
        final VerticalLayout content = new VerticalLayout(totalNumber);
        list.setColumns("id", "fileHash", "relativeFileName", "type", "format", "lastScanned");
        list.getColumn("id").setCaption("ID");
        list.getColumn("fileHash").setCaption("File Hash");
        list.getColumn("relativeFileName").setCaption("Relative File Name");
        list.getColumn("type").setCaption("Type");
        list.getColumn("format").setCaption("Format");
        list.getColumn("lastScanned").setCaption("Last Scanned");
        list.setSizeFull();
        Expander.addAndExpand(content, list);

        listEntities();

        return content;
    }

    private void listEntities() {
        final List<MediaFile> found = mediaFiles.findDuplicates();
        logger().debug("Found {} duplicate media files.", found.size());
        list.setItems(found);
        totalNumber.setValue(String.format(TOTAL_NUMBER_OF_FOUND_DUPLICATES, found.size()));
    }
}
