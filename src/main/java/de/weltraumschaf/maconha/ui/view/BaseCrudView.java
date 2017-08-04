package de.weltraumschaf.maconha.ui.view;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.Role;
import de.weltraumschaf.maconha.backend.model.entity.BaseEntity;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * Base class for a CRUD (Create, read, update, delete) view.
 * <p>
 * The view has three states it can be in and the user can navigate between the
 * states with the controls present:
 * </p>
 * <ol>
 *      <li>Initial state</li>
 *      <li><ul>
 *          <li>Form is disabled</li>
 *          <li>Nothing is selected in grid</li>
 *      </ul></li>
 *      <li>Adding an entity</li>
 *      <li><ul>
 *          <li>Form is enabled</li>
 *          <li>"Delete" has no function</li>
 *          <li>"Discard" moves to the "Initial state"</li>
 *          <li>"Save" creates the entity and moves to the "Updating an entity" state</li>
 *      </ul></li>
 *      <li>Updating an entity</li>
 *      <li><ul>
 *          <li>Entity highlighted in Grid</li>
 *          <li>Form is enabled</li>
 *          <li>"Delete" deletes the entity from the database</li>
 *          <li>"Discard" resets the form contents to what is in the database</li>
 *          <li>"Save" updates the entity and keeps the form open</li>
 *          <li>"Save" and "Discard" are only enabled when changes have been made</li>
*       </ul></li>
 * </ol>
 *
 * @param <T> the type of entity which can be edited in the view
 */
@Secured(Role.ADMIN)
public abstract class BaseCrudView<T extends BaseEntity> implements Serializable, View, HasLogger {

    public static final String CAPTION_DISCARD = "Discard";
    public static final String CAPTION_CANCEL = "Cancel";
    public static final String CAPTION_UPDATE = "Update";
    public static final String CAPTION_ADD = "Add";

    @Override
    public void enter(ViewChangeEvent event) {
        getPresenter().viewEntered(event);
    }

    @Override
    public void beforeLeave(ViewBeforeLeaveEvent event) {
        getPresenter().beforeLeavingView(event);
    }

    public final void showInitialState() {
        getForm().setEnabled(false);
        getGrid().deselectAll();
        getUpdate().setCaption(CAPTION_UPDATE);
        getCancel().setCaption(CAPTION_DISCARD);
    }

    public final void editItem(final boolean isNew) {
        if (isNew) {
            getGrid().deselectAll();
            getUpdate().setCaption(CAPTION_ADD);
            getCancel().setCaption(CAPTION_CANCEL);
            getFirstFormField().focus();
        } else {
            getUpdate().setCaption(CAPTION_UPDATE);
            getCancel().setCaption(CAPTION_DISCARD);
        }

        getForm().setEnabled(true);
        getDelete().setEnabled(!isNew);
    }

    @PostConstruct
    protected void init() {
        getGrid().addSelectionListener(e -> {
            if (!e.isUserOriginated()) {
                return;
            }

            if (e.getFirstSelectedItem().isPresent()) {
                getPresenter().editRequest(e.getFirstSelectedItem().get());
            } else {
                throw new IllegalStateException("This should never happen as deselection is not allowed");
            }
        });

        // Force user to choose save or cancel in form once enabled
        ((SingleSelectionModel<T>) getGrid().getSelectionModel()).setDeselectAllowed(false);

        // Button logic
        getUpdate().addClickListener(event -> getPresenter().updateClicked());
        getCancel().addClickListener(event -> getPresenter().cancelClicked());
        getDelete().addClickListener(event -> getPresenter().deleteClicked());
        getAdd().addClickListener(event -> getPresenter().addNewClicked());

        // Search functionality
        getSearch().addValueChangeListener(event -> getPresenter().filterGrid(event.getValue()));

    }

    public final void setDataProvider(DataProvider<T, Object> dataProvider) {
        getGrid().setDataProvider(dataProvider);
    }

    public final void setUpdateEnabled(boolean enabled) {
        getUpdate().setEnabled(enabled);
    }

    public final void setCancelEnabled(boolean enabled) {
        getCancel().setEnabled(enabled);
    }

    public final void focusField(final HasValue<?> field) {
        if (field instanceof Focusable) {
            ((Focusable) field).focus();
        } else {
            logger().warn("Unable to focus field of type " + field.getClass().getName());
        }
    }

    protected abstract BaseCrudPresenter<T, ?, ? extends BaseCrudView<T>> getPresenter();

    protected abstract Grid<T> getGrid();

    protected abstract void setGrid(Grid<T> grid);

    protected abstract Component getForm();

    protected abstract Button getAdd();

    protected abstract Button getCancel();

    protected abstract Button getDelete();

    protected abstract Button getUpdate();

    protected abstract TextField getSearch();

    protected abstract Focusable getFirstFormField();

    public abstract void bindFormFields(BeanValidationBinder<T> binder);
}