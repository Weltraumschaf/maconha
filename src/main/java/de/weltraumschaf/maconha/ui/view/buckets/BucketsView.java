package de.weltraumschaf.maconha.ui.view.buckets;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import de.weltraumschaf.maconha.app.MaconhaConfiguration;
import de.weltraumschaf.maconha.ui.view.SubView;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.repo.BucketRepo;
import de.weltraumschaf.maconha.backend.service.ScanService;
import de.weltraumschaf.maconha.backend.service.ScanServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Vie to manage {@link Bucket buckets}.
 */
@UIScope
@SpringComponent
@SpringView(name = BucketsView.VIEW_NAME)
public final class BucketsView extends SubView {
    public static final String VIEW_NAME = "buckets";
    public static final String TITLE = "Buckets";

    private static final Logger LOGGER = LoggerFactory.getLogger(BucketsView.class);
    private static final String TITLE_ID = "buckets-title";

    private MTextField filterByDirectory = new MTextField()
        .withPlaceholder("Filter by directory");
    private final Button addNew = new MButton(VaadinIcons.PLUS, "Add", this::add);
    private final Button edit = new MButton(VaadinIcons.PENCIL, "Edit", this::edit);
    private final Button delete = new ConfirmButton(VaadinIcons.TRASH, "Delete",
        "Are you sure you want to delete the entry?", this::remove);
    private final Button scan = new MButton(VaadinIcons.COGS, "Scan", this::scan);
    private final Button schedule = new MButton(VaadinIcons.ALARM, "Schedule", this::schedule);
    private final MGrid<Bucket> list = new MGrid<>(Bucket.class)
        .withProperties("id", "name", "directory")
        .withColumnHeaders("ID", "Name", "Directory")
        .withFullWidth();

    private final ScanServiceFactory scanners;
    private final BucketRepo buckets;
    private final BucketForm form;
    private final EventBus.UIEventBus events;
    private final MaconhaConfiguration config;
    private ScanService scanner;

    @Autowired
    public BucketsView(@SuppressWarnings("SpringJavaAutowiringInspection") final ScanServiceFactory scanners, final BucketRepo buckets, final BucketForm form, final EventBus.UIEventBus events, final MaconhaConfiguration config) {
        super(TITLE, TITLE_ID);
        this.scanners = scanners;
        this.buckets = buckets;
        this.form = form;
        this.events = events;
        this.config = config;
    }

    @Override
    protected void subInit() {
        scanner = scanners.create(config.getScanner());
        root.addComponent(buildContent());
        // Listen to change events emitted by PersonForm see onEvent method.
        events.subscribe(this);
    }

    private Component buildContent() {
        final MVerticalLayout content = new MVerticalLayout(
                new MHorizontalLayout(filterByDirectory, addNew, edit, delete, scan, schedule),
                list
            ).expand(list);

        listEntities();
        list.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());
        filterByDirectory.addValueChangeListener(e -> listEntities(e.getValue()));

        return content;
    }

    private void listEntities() {
        listEntities(filterByDirectory.getValue());
    }

    private void listEntities(final String nameFilter) {
        final String likeFilter = "%" + nameFilter + "%";
        list.setRows(buckets.findByDirectoryLikeIgnoreCase(likeFilter));
        adjustActionButtonState();
    }

    private void adjustActionButtonState() {
        boolean hasSelection = !list.getSelectedItems().isEmpty();
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
        scan.setEnabled(hasSelection);
        schedule.setEnabled(hasSelection);
    }

    public void add(final Button.ClickEvent event) {
        edit(new Bucket());
    }

    public void edit(final Button.ClickEvent event) {
        edit(list.asSingleSelect().getValue());
    }

    public void remove() {
        final Bucket toDelete = list.asSingleSelect().getValue();
        LOGGER.debug("Delete bucket {}.", toDelete.getName());
        buckets.delete(toDelete);
        list.deselectAll();
        listEntities();
        events.publish(this, new BucketDeleteEvent());
    }

    private void edit(final Bucket bucket) {
        form.setEntity(bucket);
        form.openInModalPopup();
    }

    public void schedule(final Button.ClickEvent event) {
        final Bucket bucket = list.asSingleSelect().getValue();
    }

    public void scan(final Button.ClickEvent event) {
        this.getUI();
        try {
            final Bucket bucket = list.asSingleSelect().getValue();
            scanner.scan(bucket, getUI());
        } catch (final ScanService.ScanError e) {
            LOGGER.error(e.getMessage(), e);
            Notification.show("Scan failed", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onBucketModified(final BucketModifiedEvent event) {
        LOGGER.debug("Event 'onBucketModified' triggered.");
        listEntities();
        form.closePopup();
    }

}
