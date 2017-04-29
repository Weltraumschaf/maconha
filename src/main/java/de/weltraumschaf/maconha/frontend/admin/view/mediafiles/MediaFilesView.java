package de.weltraumschaf.maconha.frontend.admin.view.mediafiles;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import de.weltraumschaf.maconha.frontend.admin.view.SubView;
import de.weltraumschaf.maconha.frontend.admin.view.buckets.BucketDeleteEvent;
import de.weltraumschaf.maconha.model.MediaFile;
import de.weltraumschaf.maconha.repo.MediaFileRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaFilesView.class);
    private static final String TITLE_ID = "mediafiles-title";

    // https://vaadin.com/docs/-/part/framework/components/components-grid.html
    private final MGrid<MediaFile> list = new MGrid<>(MediaFile.class)
        .withProperties("id", "type", "format", "relativeFileName", "fileHash", "lastScanned")
        .withColumnHeaders("ID", "Type", "Format", "relative File Name", "File Hash", "Last Scanned")
        .withFullWidth();

    private final MediaFileRepo mediaFiles;
    private final EventBus.UIEventBus events;

    @Autowired
    public MediaFilesView(final MediaFileRepo mediaFiles, final EventBus.UIEventBus events) {
        super(TITLE, TITLE_ID);
        this.mediaFiles = mediaFiles;
        this.events = events;
    }

    @Override
    protected void subInit() {
        root.addComponent(buildContent());
        events.subscribe(this);
    }

    private Component buildContent() {
        final MVerticalLayout content = new MVerticalLayout(
            list
        ).expand(list);
        listEntities();

        return content;
    }

    private void listEntities() {
        LOGGER.debug("List media file entities.");
        list.deselectAll();
        list.setRows(mediaFiles.findAll());
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onBucketDelete(final BucketDeleteEvent event) {
        LOGGER.debug("Event 'onBucketDelete' triggered.");
        listEntities();
    }
}
