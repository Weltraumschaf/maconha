package de.weltraumschaf.maconha.frontend.admin.view.buckets;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import de.weltraumschaf.maconha.model.Bucket;
import de.weltraumschaf.maconha.repo.BucketRepo;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 */
@UIScope
@SpringComponent
final class BucketForm extends AbstractForm<Bucket> {
    private final TextField directory = new MTextField("Directory");
    private final BucketRepo buckets;
    private final EventBus.UIEventBus events;

    BucketForm(final BucketRepo buckets, final EventBus.UIEventBus events) {
        super(Bucket.class);
        this.buckets = buckets;
        this.events = events;
        // On save & cancel, publish events that other parts of the UI can listen
        setSavedHandler(bucket -> {
            // persist changes
            buckets.save(bucket);
            // send the event for other parts of the application
            events.publish(this, new BucketModifiedEvent(bucket));
        });
        setResetHandler(bucket -> events.publish(this, new BucketModifiedEvent(bucket)));
        setSizeUndefined();
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
            new MFormLayout(
                directory
            ).withWidth(""),
            getToolbar()
        ).withWidth("");

    }
}
