package de.weltraumschaf.maconha.ui.view.buckets;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.repo.BucketRepo;
import de.weltraumschaf.maconha.backend.service.ScanService;
import de.weltraumschaf.maconha.ui.helper.Expander;
import de.weltraumschaf.maconha.ui.view.SubView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.viritin.button.ConfirmButton;

/**
 * Vie to manage {@link Bucket buckets}.
 */
@UIScope
@SpringComponent
@SpringView(name = BucketsView.VIEW_NAME)
public final class BucketsView extends SubView implements HasLogger {
    public static final String TITLE = "Buckets";
    static final String VIEW_NAME = "buckets";
    private static final String TITLE_ID = "buckets-title";

    private TextField filterByDirectory = new TextField();
    private final Button addNew = new Button("Add", VaadinIcons.PLUS);
    private final Button edit = new Button("Edit", VaadinIcons.PENCIL);
    // FIXME Remove dependency to ConfirmButton.
    private final Button delete = new ConfirmButton(VaadinIcons.TRASH, "Delete",
        "Are you sure you want to delete the entry?", this::remove);
    private final Button scan = new Button("Scan", VaadinIcons.COGS);
    private final Button schedule = new Button("Schedule", VaadinIcons.ALARM);
    private final Grid<Bucket> bucketList = new Grid<>(Bucket.class);

    private final BucketRepo buckets;
    private final BucketForm editForm;
    private final EventBus.UIEventBus events;
    private ScanService scanner;

    @Autowired
    public BucketsView(@SuppressWarnings("SpringJavaAutowiringInspection") final ScanService scanner, final BucketRepo buckets, final BucketForm form, final EventBus.UIEventBus events) {
        super(TITLE, TITLE_ID);
        this.scanner = scanner;
        this.buckets = buckets;
        this.editForm = form;
        this.events = events;
        this.addNew.addClickListener(this::add);
        this.edit.addClickListener(this::edit);
        this.scan.addClickListener(this::scan);
        this.schedule.addClickListener(this::schedule);
    }

    @Override
    protected void subInit() {
        root.addComponent(buildContent());
        // Listen to change events emitted by PersonForm see onEvent method.
        events.subscribe(this);
    }

    private Component buildContent() {
        filterByDirectory.setPlaceholder("Filter by directory");

        bucketList.setColumns("id", "name", "directory");
        bucketList.getColumn("id").setCaption("ID");
        bucketList.getColumn("name").setCaption("Name");
        bucketList.getColumn("directory").setCaption("Directory");
        bucketList.setSizeFull();

        final VerticalLayout content = new VerticalLayout(
            new HorizontalLayout(filterByDirectory, addNew, edit, delete, scan, schedule)
        );
        Expander.addAndExpand(content, bucketList);

        listEntities();
        bucketList.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());
        filterByDirectory.addValueChangeListener(e -> listEntities(e.getValue()));

        return content;
    }

    private void listEntities() {
        listEntities(filterByDirectory.getValue());
    }

    private void listEntities(final String nameFilter) {
        final String likeFilter = "%" + nameFilter + "%";
        bucketList.setItems(buckets.findByDirectoryLikeIgnoreCase(likeFilter));
        adjustActionButtonState();
    }

    private void adjustActionButtonState() {
        boolean hasSelection = !bucketList.getSelectedItems().isEmpty();
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
        scan.setEnabled(hasSelection);
        schedule.setEnabled(hasSelection);
    }

    public void add(final Button.ClickEvent event) {
        edit(new Bucket());
    }

    public void edit(final Button.ClickEvent event) {
        edit(bucketList.asSingleSelect().getValue());
    }

    public void remove() {
        final Bucket toDelete = bucketList.asSingleSelect().getValue();
        logger().debug("Delete bucket {}.", toDelete.getName());
        buckets.delete(toDelete);
        bucketList.deselectAll();
        listEntities();
        events.publish(this, new BucketDeleteEvent());
    }

    private void edit(final Bucket bucket) {
        editForm.setModalWindowTitle("Edit bucket " + bucket.getName());
        editForm.setEntity(bucket);
        editForm.openInModalPopup();
    }

    public void schedule(final Button.ClickEvent event) {
        final Bucket bucket = bucketList.asSingleSelect().getValue();
    }

    public void scan(final Button.ClickEvent event) {
        this.getUI();
        try {
            final Bucket bucket = bucketList.asSingleSelect().getValue();
            scanner.scan(bucket, getUI());
        } catch (final ScanService.ScanError e) {
            logger().error(e.getMessage(), e);
            Notification.show("Scan failed", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onBucketModified(final BucketModifiedEvent event) {
        logger().debug("Event 'onBucketModified' triggered.");
        listEntities();
        editForm.closePopup();
    }

}
