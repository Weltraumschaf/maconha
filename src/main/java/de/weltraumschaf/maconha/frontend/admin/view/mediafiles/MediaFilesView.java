package de.weltraumschaf.maconha.frontend.admin.view.mediafiles;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import de.weltraumschaf.maconha.frontend.admin.view.SubView;
import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Vie to manage {@link MediaFile media files}.
 */
@UIScope
@SpringComponent
@SpringView(name = MediaFilesView.VIEW_NAME)
public final class MediaFilesView extends SubView {
    public static final String VIEW_NAME = "mediafiles";
    public static final String TITLE = "Media Files";

    private static final String TITLE_ID = "mediafiles-title";

    private final MGrid<MediaFile> list = new MGrid<>(MediaFile.class)
        .withProperties("id", "type", "format", "relativeFileName", "fileHash", "lastScanned")
        .withColumnHeaders("ID", "Type", "Format", "relative File Name", "File Hash", "Last Scanned")
        .withFullWidth();

    private final MediaFileRepo mediaFiles;

    @Autowired
    public MediaFilesView(final MediaFileRepo mediaFiles) {
        super(TITLE, TITLE_ID);
        this.mediaFiles = mediaFiles;
    }

    @Override
    protected void subInit() {
        root.addComponent(buildContent());
    }

    private Component buildContent() {
        final MVerticalLayout content = new MVerticalLayout(
            list
        ).expand(list);
        listEntities();

        return content;
    }

    private void listEntities() {
        list.setRows(mediaFiles.findAll());
    }
}
