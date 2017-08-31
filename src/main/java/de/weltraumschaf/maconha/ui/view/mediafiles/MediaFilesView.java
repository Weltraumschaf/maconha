package de.weltraumschaf.maconha.ui.view.mediafiles;

import com.vaadin.data.HasValue;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.FileExtension;
import de.weltraumschaf.maconha.backend.model.MediaType;
import de.weltraumschaf.maconha.backend.model.entity.MediaFile;
import de.weltraumschaf.maconha.backend.repo.MediaFileRepo;
import de.weltraumschaf.maconha.ui.helper.Expander;
import de.weltraumschaf.maconha.ui.view.SubView;
import de.weltraumschaf.maconha.ui.view.buckets.BucketDeleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;

import java.util.EnumSet;
import java.util.List;

/**
 * View to manage {@link MediaFile media files}.
 */
@UIScope
@SpringComponent
@SpringView(name = MediaFilesView.VIEW_NAME)
public final class MediaFilesView extends SubView implements HasLogger {
    public static final String TITLE = "Media Files";
    static final String VIEW_NAME = "mediafiles";
    private static final String TITLE_ID = "mediafiles-title";

    private static final String TOTAL_NUMBER_OF_FOUND_MEDIA_FILES = "Total number of found media files: %d";

    private TextField filterByRelativeFileName = new TextField("Filter by relative file name");
    private final ComboBox<MediaType> filterByMediaType = new ComboBox<>("Filter by type");
    private final ComboBox<FileExtension> filterByFileExtension = new ComboBox<>("Filter by format");
    private Label totalNumber = new Label(String.format(TOTAL_NUMBER_OF_FOUND_MEDIA_FILES, 0));
    private final Grid<MediaFile> list = new Grid<>(MediaFile.class);

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
        filterByRelativeFileName.setPlaceholder("file name");
        filterByMediaType.setItems(EnumSet.allOf(MediaType.class));
        filterByFileExtension.setItems(EnumSet.allOf(FileExtension.class));

        final VerticalLayout content = new VerticalLayout(
            new HorizontalLayout(filterByRelativeFileName, filterByMediaType, filterByFileExtension),
            totalNumber
        );

        list.setColumns("id", "type", "format", "relativeFileName", "fileHash", "lastScanned");
        list.getColumn("id").setCaption("ID");
        list.getColumn("type").setCaption("Type");
        list.getColumn("format").setCaption("Format");
        list.getColumn("relativeFileName").setCaption("relative File Name");
        list.getColumn("fileHash").setCaption("File Hash");
        list.getColumn("lastScanned").setCaption("Last Scanned");
        list.setSizeFull();
        Expander.addAndExpand(content, list);

        listEntities();

        filterByRelativeFileName.addValueChangeListener(this::listEntities);
        filterByMediaType.addValueChangeListener(this::listEntities);
        filterByFileExtension.addValueChangeListener(this::listEntities);
        return content;
    }

    private void listEntities() {
        listEntities(null);
    }

    private void listEntities(final HasValue.ValueChangeEvent<?> event) {
        final List<MediaFile> found;

        if (event == null) {
            found = mediaFiles.findAll();
        } else {
            final String relativeFileNameFilter = filterByRelativeFileName.getValue().trim();
            final MediaType mediaTypeFilter = filterByMediaType.getValue();
            final FileExtension fileExtensionFilter = filterByFileExtension.getValue();
            logger().debug("Filter media files by '{}' and '{}' and '{}'.",
                relativeFileNameFilter, mediaTypeFilter, fileExtensionFilter);

            if (relativeFileNameFilter.isEmpty() && mediaTypeFilter == null && fileExtensionFilter == null) {
                found = mediaFiles.findAll();
            } else {
                logger().debug("Use specification to search media files.");
                final Specification<MediaFile> specification = MediaFileRepo.MediaFileSpecifications
                    .relativeFileNameIgnoreCaseAndTypeAndFormat(
                        relativeFileNameFilter,
                        mediaTypeFilter,
                        fileExtensionFilter);
                found = mediaFiles.findAll(specification);
            }
        }

        logger().debug("Found {} media files.", found.size());
        list.setItems(found);
        totalNumber.setValue(String.format(TOTAL_NUMBER_OF_FOUND_MEDIA_FILES, found.size()));
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onBucketDelete(final BucketDeleteEvent event) {
        logger().debug("Event 'onBucketDelete' triggered.");
        listEntities();
    }
}
