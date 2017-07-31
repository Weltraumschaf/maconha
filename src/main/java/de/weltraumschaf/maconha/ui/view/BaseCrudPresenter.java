package de.weltraumschaf.maconha.ui.view;

import com.vaadin.data.*;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import de.weltraumschaf.maconha.app.BeanLocator;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.backend.model.entity.BaseEntity;
import de.weltraumschaf.maconha.backend.service.CrudService;
import de.weltraumschaf.maconha.backend.service.UserFriendlyDataException;
import de.weltraumschaf.maconha.ui.components.ConfirmPopup;
import de.weltraumschaf.maconha.ui.navigation.NavigationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

public abstract class BaseCrudPresenter<T extends BaseEntity, S extends CrudService<T>, V extends BaseCrudView<T>>
    implements HasLogger, Serializable {

    private final NavigationManager navigation;
    private final S service;
    @Autowired
    private BeanLocator locator;
    private V view;
    private FilterablePageableDataProvider<T, Object> provider;
    private BeanValidationBinder<T> binder;
    private T editItem;

    protected BaseCrudPresenter(final FilterablePageableDataProvider<T, Object> provider, final NavigationManager navigation, final S service) {
        super();
        this.provider = provider;
        this.navigation = navigation;
        this.service = service;
    }

    public final void viewEntered(ViewChangeEvent event) {
        if (!event.getParameters().isEmpty()) {
            editRequest(event.getParameters());
        }
    }

    public final void beforeLeavingView(ViewBeforeLeaveEvent event) {
        runWithConfirmation(event::navigate, () -> {
            // Nothing special needs to be done if user aborts the navigation
        });
    }

    @PostConstruct
    public final void createBinder() {
        binder = new BeanValidationBinder<>(getEntityType());
        binder.addStatusChangeListener(this::onFormStatusChange);
    }

    protected final BeanValidationBinder<T> getBinder() {
        return binder;
    }

    protected final S getService() {
        return service;
    }

    protected final void filterGrid(final String filter) {
        provider.setFilter(filter);
    }

    protected final T loadEntity(long id) {
        return service.load(id);
    }

    @SuppressWarnings("unchecked")
    protected final Class<T> getEntityType() {
        // FIXME Extremely dirty because it relies on the position here in the source file!
        return (Class<T>) ResolvableType.forClass(getClass()).getSuperType().getGeneric(0).resolve();
    }

    private T createEntity() {
        try {
            return getEntityType().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnsupportedOperationException(
                "Entity of type " + getEntityType().getName() + " is missing a public no-args constructor", e);
        }
    }

    protected void deleteEntity(final T entity) {
        if (entity.isNew()) {
            throw new IllegalArgumentException("Cannot delete an entity which is not in the database");
        } else {
            service.delete(entity.getId());
        }
    }

    public void init(final V view) {
        this.view = view;
        view.setDataProvider(provider);
        view.bindFormFields(getBinder());
        view.showInitialState();
    }

    protected V getView() {
        return view;
    }

    public final void editRequest(final String parameters) {
        long id;

        try {
            id = Long.parseLong(parameters);
        } catch (final NumberFormatException e) {
            id = -1;
        }

        if (id == -1) {
            editItem(createEntity());
        } else {
            selectAndEditEntity(loadEntity(id));
        }
    }

    private void selectAndEditEntity(final T entity) {
        getView().getGrid().select(entity);
        editRequest(entity);
    }

    public final void editRequest(final T entity) {
        runWithConfirmation(() -> {
            // Fetch a fresh item so we have the latest changes (less optimistic
            // locking problems)
            final T freshEntity = loadEntity(entity.getId());
            editItem(freshEntity);
        }, () -> {
            // Revert selection in grid
            final Grid<T> grid = getView().getGrid();

            if (editItem == null) {
                grid.deselectAll();
            } else {
                grid.select(editItem);
            }
        });
    }

    protected void editItem(final T item) {
        if (item == null) {
            throw new IllegalArgumentException("The entity to edit cannot be null");
        }

        this.editItem = item;
        final boolean isNew = item.isNew();

        if (isNew) {
            navigation.updateViewParameter("new");
        } else {
            navigation.updateViewParameter(String.valueOf(item.getId()));
        }

        getBinder().readBean(editItem);
        getView().editItem(isNew);
    }

    public final void addNewClicked() {
        runWithConfirmation(() -> {
            final T entity = createEntity();
            editItem(entity);
        }, () -> {
            // Do nothing on cancel.
        });
    }

    /**
     * Runs the given command if the form contains no unsaved changes or if the
     * user clicks ok in the confirmation dialog telling about unsaved changes.
     *
     * @param onConfirmation the command to run if there are not changes or user pushes
     *                       {@literal confirm}
     * @param onCancel       the command to run if there are changes and the user pushes
     *                       {@literal cancel}
     */
    private void runWithConfirmation(final Runnable onConfirmation, final Runnable onCancel) {
        if (hasUnsavedChanges()) {
            locator.find(ConfirmPopup.class).showLeaveViewConfirmDialog(view, onConfirmation, onCancel);
        } else {
            onConfirmation.run();
        }
    }

    private boolean hasUnsavedChanges() {
        return editItem != null && getBinder().hasChanges();
    }

    public final void updateClicked() {
        try {
            // The validate() call is needed only to ensure that the error
            // indicator is properly shown for the field in case of an error
            getBinder().validate();
            getBinder().writeBean(editItem);
        } catch (final ValidationException e) {
            // Commit failed because of validation errors
            final List<BindingValidationStatus<?>> fieldErrors = e.getFieldValidationErrors();

            if (!fieldErrors.isEmpty()) {
                // Field level error
                final HasValue<?> firstErrorField = fieldErrors.get(0).getField();
                getView().focusField(firstErrorField);
            } else {
                // Bean validation error
                final ValidationResult firstError = e.getBeanValidationErrors().get(0);
                Notification.show(firstError.getErrorMessage(), Type.ERROR_MESSAGE);
            }

            return;
        }

        final boolean isNew = editItem.isNew();
        final T entity;

        try {
            entity = service.save(editItem);
        } catch (OptimisticLockingFailureException e) {
            // Somebody else probably edited the data at the same time
            Notification.show(
                "Somebody else might have updated the data. Please refresh and try again.",
                Type.ERROR_MESSAGE);
            logger().debug("Optimistic locking error while saving entity of type " + editItem.getClass().getName(), e);
            return;
        } catch (UserFriendlyDataException e) {
            Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
            logger().debug("Unable to update entity of type " + editItem.getClass().getName(), e);
            return;
        } catch (Exception e) {
            // Something went wrong, no idea what
            Notification.show("A problem occured while saving the data. Please check the fields.", Type.ERROR_MESSAGE);
            logger().error("Unable to save entity of type " + editItem.getClass().getName(), e);
            return;
        }

        if (isNew) {
            // Move to the "Updating an entity" state
            provider.refreshAll();
            selectAndEditEntity(entity);
        } else {
            // Stay in the "Updating an entity" state
            provider.refreshItem(entity);
            editRequest(entity);
        }
    }

    public final void cancelClicked() {
        if (editItem.isNew()) {
            revertToInitialState();
        } else {
            editItem(editItem);
        }
    }

    private void revertToInitialState() {
        editItem = null;
        getBinder().readBean(null);
        getView().showInitialState();
        navigation.updateViewParameter("");
    }

    public final void deleteClicked() {
        try {
            deleteEntity(editItem);
        } catch (final UserFriendlyDataException e) {
            Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
            logger().debug("Unable to delete entity of type " + editItem.getClass().getName(), e);
            return;
        } catch (final DataIntegrityViolationException e) {
            Notification.show(
                "The given entity cannot be deleted as there are references to it in the database",
                Type.ERROR_MESSAGE);
            logger().error("Unable to delete entity of type " + editItem.getClass().getName(), e);
            return;
        }

        provider.refreshAll();
        revertToInitialState();
    }

    public final void onFormStatusChange(final StatusChangeEvent event) {
        boolean hasChanges = event.getBinder().hasChanges();
        boolean hasValidationErrors = event.hasValidationErrors();
        getView().setUpdateEnabled(hasChanges && !hasValidationErrors);
        getView().setCancelEnabled(hasChanges);
    }

}
