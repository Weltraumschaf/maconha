package de.weltraumschaf.maconha.ui.components;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;
import de.weltraumschaf.commons.validate.Validate;

import java.io.Serializable;

/**
 * @author mstahv
 */
public abstract class BaseForm<T> extends CustomComponent {
    private boolean settingBean;
    private Button deleteButton;
    private Button saveButton;
    private Button resetButton;
    private T entity;
    private BaseForm.SavedHandler<T> savedHandler;
    private BaseForm.ResetHandler<T> resetHandler;
    private BaseForm.DeleteHandler<T> deleteHandler;
    private String modalWindowTitle = "Edit entry";
    private Window popup;
    private Binder<T> binder;
    private boolean hasChanges;

    public BaseForm(final Class<T> entityType) {
        addAttachListener(event -> lazyInit());
        binder = new BeanValidationBinder<>(entityType);
        binder.addValueChangeListener(e -> {
            // binder.hasChanges is not really usefull so track it manually
            if (!settingBean) {
                hasChanges = true;
            }
        });
        binder.addStatusChangeListener(e -> {
            // TODO optimize this
            // TODO see if explicitly calling writeBean would write also invalid
            // values -> would make functionality more logical and easier for
            // users to do validation and error reporting

            // Eh, value change listener is called after status change listener, so
            // ensure flag is on...
            if (!settingBean) {
                hasChanges = true;
            }
            adjustResetButtonState();
            adjustSaveButtonState();
        });
    }

    /**
     * Sets the object to be edited by this form. This method binds all fields
     * from this form to given objects.
     * <p>
     * If your form needs to manually configure something based on the state of
     * the edited object, you can override this method to do that either before
     * the object is bound to fields or to do something after the bean binding.
     *
     * @param entity the object to be edited by this form
     */
    public void setEntity(T entity) {
        this.entity = entity;
        this.settingBean = true;
        lazyInit();

        if (entity != null) {
            binder.setBean(entity);
            hasChanges = false;
            setVisible(true);
        } else {
            binder.setBean(null);
            hasChanges = false;
            setVisible(false);
        }

        settingBean = false;
    }

    /**
     * @return true if bean has been changed since last setEntity call.
     */
    private boolean hasChanges() {
        return hasChanges;
    }

    protected void setSavedHandler(final BaseForm.SavedHandler<T> savedHandler) {
        this.savedHandler = Validate.notNull(savedHandler, "savedHandler");
        getSaveButton().setVisible(true);
    }

    protected void setResetHandler(final BaseForm.ResetHandler<T> resetHandler) {
        this.resetHandler = Validate.notNull(resetHandler, "resetHandler");
        getResetButton().setVisible(true);
    }

    protected void setDeleteHandler(final BaseForm.DeleteHandler<T> deleteHandler) {
        this.deleteHandler = Validate.notNull(deleteHandler, "deleteHandler");
        getDeleteButton().setVisible(true);
    }

    private String getModalWindowTitle() {
        return modalWindowTitle;
    }

    public void setModalWindowTitle(String modalWindowTitle) {
        this.modalWindowTitle = modalWindowTitle;
    }

    private void lazyInit() {
        if (getCompositionRoot() == null) {
            setCompositionRoot(createContent());
            bind();
        }
    }

    /**
     * By default just does simple name based binding. Override this method to
     * customize the binding.
     */
    protected void bind() {
        binder.bindInstanceFields(this);
    }

    /**
     * This method should return the actual content of the form, including possible toolbar.
     *
     * <p>
     * Use setEntity(T entity) to fill in the data. Am example implementation
     * could look like this:
     * </p>
     *
     * <pre>{@code
     * public class PersonForm extends AbstractForm&lt;Person&gt; {
     *
     *     private TextField firstName = new TextField(&quot;First Name&quot;);
     *     private TextField lastName = new TextField(&quot;Last Name&quot;);
     *
     *     &#064;Override
     *     protected Component createContent() {
     *         return new VerticalLayout(
     *                 new FormLayout(
     *                         firstName,
     *                         lastName
     *                 ),
     *                 getToolbar()
     *         );
     *     }
     * }
     * }</pre>
     *
     * @return the content of the form
     */
    protected abstract Component createContent();

    protected boolean isBound() {
        return binder != null && binder.getBean() != null;
    }

    private void adjustSaveButtonState() {
        if (isBound()) {
            boolean valid = binder.isValid();
            getSaveButton().setEnabled(hasChanges() && valid);
        }
    }

    private Button getSaveButton() {
        if (saveButton == null) {
            saveButton = new PrimaryButton("Save");
            saveButton.setVisible(false);
            saveButton.addClickListener(this::save);
        }

        return saveButton;
    }


    private Button getResetButton() {
        if (resetButton == null) {
            resetButton = new Button("Cancel");
            resetButton.setVisible(false);
            resetButton.addClickListener(this::reset);
        }

        return resetButton;
    }

    private Button getDeleteButton() {
        if (deleteButton == null) {
            deleteButton = new DeleteButton("Delete");
            deleteButton.setVisible(false);
            deleteButton.addClickListener(this::delete);
        }

        return deleteButton;
    }

    private void adjustResetButtonState() {
        if (popup != null && popup.getParent() != null) {
            // Assume cancel button in a form opened to a popup also closes
            // it, allows closing via cancel button by default
            getResetButton().setEnabled(true);
            return;
        }

        if (isBound()) {
            boolean modified = hasChanges();
            getResetButton().setEnabled(modified || popup != null);
        }
    }

    /**
     * Get the currently edited entity.
     *
     * @return {@code null} if the form is currently unbound
     */
    public T getEntity() {
        return entity;
    }

    protected void save(final Button.ClickEvent e) {
        savedHandler.onSave(getEntity());
        hasChanges = false;
        adjustSaveButtonState();
        adjustResetButtonState();
    }

    protected void reset(final Button.ClickEvent e) {
        resetHandler.onReset(getEntity());
        hasChanges = false;
        adjustSaveButtonState();
        adjustResetButtonState();
    }

    protected void delete(final Button.ClickEvent e) {
        deleteHandler.onDelete(getEntity());
        hasChanges = false;
    }

    /**
     * @return A default toolbar containing save/cancel/delete buttons
     */
    protected HorizontalLayout getToolbar() {
        return new HorizontalLayout(
            getSaveButton(),
            getResetButton(),
            getDeleteButton()
        );
    }

    public void openInModalPopup() {
        popup = new Window(getModalWindowTitle(), this);
        popup.setModal(true);
        UI.getCurrent().addWindow(popup);
        focusFirst();
    }

    /**
     * Focuses the first field found from the form. It often improves UX to call
     * this method, or focus another field, when you assign a bean for editing.
     */
    private void focusFirst() {
        Component compositionRoot = getCompositionRoot();
        findFieldAndFocus(compositionRoot);
    }

    private boolean findFieldAndFocus(final Component compositionRoot) {
        if (compositionRoot instanceof AbstractComponentContainer) {
            final AbstractComponentContainer cc = (AbstractComponentContainer) compositionRoot;

            for (final Component component : cc) {
                if (component instanceof AbstractTextField) {
                    AbstractTextField abstractTextField = (AbstractTextField) component;
                    abstractTextField.selectAll();
                    return true;
                }

                if (component instanceof AbstractField) {
                    AbstractField abstractField = (AbstractField) component;
                    abstractField.focus();
                    return true;
                }

                if (component instanceof AbstractComponentContainer) {
                    if (findFieldAndFocus(component)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void closePopup() {
        if (popup != null) {
            popup.close();
        }
    }

    public interface SavedHandler<T> extends Serializable {
        void onSave(T entity);
    }

    public interface ResetHandler<T> extends Serializable {
        void onReset(T entity);
    }

    public interface DeleteHandler<T> extends Serializable {
        void onDelete(T entity);
    }

}

