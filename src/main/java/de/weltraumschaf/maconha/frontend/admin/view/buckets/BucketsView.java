package de.weltraumschaf.maconha.frontend.admin.view.buckets;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.repo.BucketRepo;
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

import javax.annotation.PostConstruct;

/**
 * Vie to manage {@link Bucket buckets}.
 */
@UIScope
@SpringComponent
@SpringView(name = BucketsView.VIEW_NAME)
public final class BucketsView extends Panel implements View {
    public static final String VIEW_NAME = "buckets";

    private final MGrid<Bucket> list = new MGrid<>(Bucket.class)
        .withProperties("id", "directory")
        .withColumnHeaders("id", "directory")
        .withFullWidth();
    private MTextField filterByDirectory = new MTextField()
        .withPlaceholder("Filter by directory");
    private final Button addNew = new MButton(VaadinIcons.PLUS, this::add);
    private final Button edit = new MButton(VaadinIcons.PENCIL, this::edit);
    private final Button delete = new ConfirmButton(VaadinIcons.TRASH,
        "Are you sure you want to delete the entry?", this::remove);

    @Autowired
    private BucketRepo buckets;
    @Autowired
    private BucketForm form;
    @Autowired
    private EventBus.UIEventBus events;

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method.
    }

    @PostConstruct
    public void init() {
        setContent(
            new MVerticalLayout(
                new MHorizontalLayout(filterByDirectory, addNew, edit, delete),
                list
            ).expand(list)
        );
        listEntities();
        list.asSingleSelect().addValueChangeListener(e -> adjustActionButtonState());
        filterByDirectory.addValueChangeListener(e -> {
            listEntities(e.getValue());
        });

        // Listen to change events emitted by PersonForm see onEvent method
        events.subscribe(this);
    }

    private void listEntities() {
        listEntities(filterByDirectory.getValue());
    }

    private void listEntities(String nameFilter) {
        String likeFilter = "%" + nameFilter + "%";
        list.setRows(buckets.findByDirectoryLikeIgnoreCase(likeFilter));
        adjustActionButtonState();
    }

    private void adjustActionButtonState() {
        boolean hasSelection = !list.getSelectedItems().isEmpty();
        edit.setEnabled(hasSelection);
        delete.setEnabled(hasSelection);
    }

    public void add(Button.ClickEvent clickEvent) {
        edit(new Bucket());
    }

    public void edit(Button.ClickEvent e) {
        edit(list.asSingleSelect().getValue());
    }

    public void remove() {
        buckets.delete(list.asSingleSelect().getValue());
        list.deselectAll();
        listEntities();
    }

    private void edit(final Bucket phoneBookEntry) {
        form.setEntity(phoneBookEntry);
        form.openInModalPopup();
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onBucketModified(final BucketModifiedEvent event) {
        listEntities();
        form.closePopup();
    }

}
