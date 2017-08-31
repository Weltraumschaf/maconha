package de.weltraumschaf.maconha.ui.components;

import com.vaadin.server.Resource;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Button;
import de.weltraumschaf.commons.validate.Validate;

/**
 * This button is for delete actions and asks for a confirmation before executing the attached listener.
 */
public final class DeleteButton extends Button {

    public DeleteButton(final String caption) {
        super(Validate.notEmpty(caption, "caption"));
    }

    /**
     * Dedicated constructor.
     *
     * @param caption must not be {@code null} or empty
     * @param icon must not be {@code null}
     */
    public DeleteButton(final String caption, final Resource icon) {
        super(Validate.notEmpty(caption, "caption"), Validate.notNull(icon, "icon"));
    }

    @Override
    protected void fireClick(final MouseEventDetails details) {
        new ConfirmPopup().showDeleteConfirmDialog(this, () -> {
            super.fireClick(details);
        }, () -> {
            // Nothing to do on cancel.
        });
    }
}
