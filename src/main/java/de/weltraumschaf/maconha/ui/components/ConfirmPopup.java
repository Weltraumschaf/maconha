package de.weltraumschaf.maconha.ui.components;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Component;
import de.weltraumschaf.maconha.app.BeanLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.spring.annotation.PrototypeScope;

public class ConfirmPopup {

    private final ConfirmDialogFactory confirmDialogFactory = new ConfirmDialogFactory();

    /**
     * Shows the standard before leave confirm dialog on given ui. If the user
     * confirms the the navigation, the given {@literal runOnConfirm} will be
     * executed. Otherwise, nothing will be done.
     *
     * @param view         the view in which to show the dialog
     * @param runOnConfirm the runnable to execute if the user presses {@literal confirm}
     *                     in the dialog
     */
    public void showLeaveViewConfirmDialog(View view, Runnable runOnConfirm) {
        showLeaveViewConfirmDialog(view, runOnConfirm, () -> {
            // Do nothing on cancel
        });
    }

    /**
     * Shows the standard before leave confirm dialog on given ui. If the user
     * confirms the the navigation, the given {@literal runOnConfirm} will be
     * executed. Otherwise, the given {@literal runOnCancel} runnable will be
     * executed.
     *
     * @param view         the view in which to show the dialog
     * @param runOnConfirm the runnable to execute if the user presses {@literal confirm}
     *                     in the dialog
     * @param runOnCancel  the runnable to execute if the user presses {@literal cancel}
     *                     in the dialog
     */
    public void showLeaveViewConfirmDialog(final View view, final Runnable runOnConfirm, final Runnable runOnCancel) {
        final ConfirmDialog dialog = confirmDialogFactory.create(
            "Please confirm",
            "You have unsaved changes that will be discarded if you navigate away.",
            "Discard Changes",
            "Cancel",
            null);

        dialog.show(view.getViewComponent().getUI(), event -> {
            if (event.isConfirmed()) {
                runOnConfirm.run();
            } else {
                runOnCancel.run();
            }
        }, true);
    }

    public void showDeleteConfirmDialog(final Component component, final Runnable runOnConfirm, final Runnable runOnCancel) {
        final ConfirmDialog dialog = confirmDialogFactory.create(
            "Please confirm",
            "Do you really want to delete this item?",
            "Delete",
            "Cancel",
            null);

        dialog.show(component.getUI(), event -> {
            if (event.isConfirmed()) {
                runOnConfirm.run();
            } else {
                runOnCancel.run();
            }
        }, true);

    }
}
