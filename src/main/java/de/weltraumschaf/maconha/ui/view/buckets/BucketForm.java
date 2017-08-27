package de.weltraumschaf.maconha.ui.view.buckets;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import de.weltraumschaf.maconha.backend.model.entity.Bucket;
import de.weltraumschaf.maconha.backend.repo.BucketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;

/**
 * Form to edit buckets.
 */
@UIScope
@SpringComponent
final class BucketForm extends AbstractForm<Bucket> {
    private final TextField name = new MTextField("Name");
    private final TextField directory = new MTextField("Directory");
    private final BucketRepo buckets;
    private final EventBus.UIEventBus events;

    @Autowired
    public BucketForm(final BucketRepo buckets, final EventBus.UIEventBus events) {
        super(Bucket.class);
        this.buckets = buckets;
        this.events = events;
    }

    @PostConstruct
    public void init() {
        // On save & cancel, publish events that other parts of the UI can listen.
        setSavedHandler(bucket -> {
            // Persist changes.
            buckets.save(bucket);
            // Send the event for other parts of the application.
            events.publish(this, new BucketModifiedEvent());
        });
        setResetHandler(bucket -> events.publish(this, new BucketModifiedEvent()));
        setSizeUndefined();
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
            new MFormLayout(name, directory).withWidth(""),
            getToolbar()
        ).withWidth("");

    }
}
