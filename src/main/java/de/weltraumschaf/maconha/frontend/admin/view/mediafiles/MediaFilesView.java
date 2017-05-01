package de.weltraumschaf.maconha.frontend.admin.view.mediafiles;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
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
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * View to manage {@link MediaFile media files}.
 */
@UIScope
@SpringComponent
@SpringView(name = MediaFilesView.VIEW_NAME)
public final class MediaFilesView extends SubView {
    public static final String VIEW_NAME = "mediafiles";
    public static final String TITLE = "Media Files";

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaFilesView.class);
    private static final String TITLE_ID = "mediafiles-title";

    private static final String TOTAL_NUMBER_OF_FOUND_MEDIA_FILES = "Total number of found media files: %s";

    private MTextField filterByRelativeFileName = new MTextField()
        .withPlaceholder("relative File Name");
    private Label totalNumber = new Label(String.format(TOTAL_NUMBER_OF_FOUND_MEDIA_FILES, 0));
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

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        listEntities();
    }

    private Component buildContent() {
        final MVerticalLayout content = new MVerticalLayout(
            new MHorizontalLayout(filterByRelativeFileName),
            totalNumber,
            list).expand(list);
        listEntities();
        filterByRelativeFileName.addValueChangeListener(e -> listEntities(e.getValue()));

        return content;
    }


    private void listEntities() {
        listEntities(filterByRelativeFileName.getValue());
    }

    private void listEntities(final String relativeFileNameFilter) {
        LOGGER.debug("List media file entities.");
        final String likeFilter = "%" + relativeFileNameFilter + "%";
        final List<MediaFile> found = mediaFiles.findByRelativeFileNameLikeIgnoreCase(likeFilter);
        list.setRows(found);
        totalNumber.setValue(String.format(TOTAL_NUMBER_OF_FOUND_MEDIA_FILES, found.size()));
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onBucketDelete(final BucketDeleteEvent event) {
        LOGGER.debug("Event 'onBucketDelete' triggered.");
        listEntities();
    }
}
