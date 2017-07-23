package de.weltraumschaf.maconha.ui.view.duplicates;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import de.weltraumschaf.maconha.ui.view.SubView;
import de.weltraumschaf.maconha.backend.model.MediaFile;
import de.weltraumschaf.maconha.backend.repo.MediaFileRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * View to manage duplicate media files.
 */
@UIScope
@SpringComponent
@SpringView(name = DuplicatesView.VIEW_NAME)
public final class DuplicatesView extends SubView {
    public static final String VIEW_NAME = "duplicates";
    public static final String TITLE = "Duplicates";

    private static final Logger LOGGER = LoggerFactory.getLogger(DuplicatesView.class);
    private static final String TITLE_ID = "duplicates-title";
    private static final String TOTAL_NUMBER_OF_FOUND_DUPLICATES = "Total number of found duplicates: %d";

    private final Label totalNumber = new Label(String.format(TOTAL_NUMBER_OF_FOUND_DUPLICATES, 0));
    private final MGrid<MediaFile> list = new MGrid<>(MediaFile.class)
        .withProperties("id", "fileHash", "relativeFileName", "type", "format", "lastScanned")
        .withColumnHeaders("ID", "File Hash", "relative File Name", "Type", "Format", "Last Scanned")
        .withFullWidth();
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
        final MVerticalLayout content = new MVerticalLayout(
            totalNumber,
            list).expand(list);
        listEntities();

        return content;
    }

    private void listEntities() {
        final List<MediaFile> found = mediaFiles.findDuplicates();
        LOGGER.debug("Found {} duplicate media files.", found.size());
        list.setRows(found);
        totalNumber.setValue(String.format(TOTAL_NUMBER_OF_FOUND_DUPLICATES, found.size()));
    }
}
