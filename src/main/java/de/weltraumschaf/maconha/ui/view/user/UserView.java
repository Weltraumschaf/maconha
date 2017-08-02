package de.weltraumschaf.maconha.ui.view.user;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import de.weltraumschaf.maconha.backend.model.entity.User;
import de.weltraumschaf.maconha.ui.view.BaseCrudView;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView
public class UserView extends BaseCrudView<User> {

    private final UserViewDesign design = new UserViewDesign();
    private final UserPresenter presenter;

    private boolean passwordRequired;

    /**
     * Custom validator to be able to decide dynamically whether the password
     * field is required or not (empty value when updating the user is
     * interpreted as 'do not change the password').
     */
    private final Validator<String> passwordValidator = new Validator<String>() {
        final BeanValidator passwordBeanValidator = new BeanValidator(User.class, "password");

        @Override
        public ValidationResult apply(final String value, final ValueContext context) {
            if (!passwordRequired && value.isEmpty()) {
                // No password required and field is empty
                // OK regardless of other restrictions as the empty value will
                // not be used
                return ValidationResult.ok();
            } else {
                return passwordBeanValidator.apply(value, context);
            }
        }
    };

    @Autowired
    public UserView(final UserPresenter presenter) {
        super();
        this.presenter = presenter;
    }

    @Override
    public void init() {
        super.init();
        presenter.init(this);
        getGrid().setColumns("email", "name", "role");
    }

    @Override
    public void bindFormFields(final BeanValidationBinder<User> binder) {
        binder.forField(getViewComponent().password)
            .withValidator(passwordValidator)
            .bind(bean -> "", (bean, value) -> {
                // If nothing is entered in the password field, do  nothing.
                if (!value.isEmpty()) {
                    bean.setPassword(presenter.encodePassword(value));
                }
            });
        binder.bindInstanceFields(getViewComponent());
    }

    public void setPasswordRequired(final boolean passwordRequired) {
        this.passwordRequired = passwordRequired;
        getViewComponent().password.setRequiredIndicatorVisible(passwordRequired);
    }

    @Override
    public UserViewDesign getViewComponent() {
        return design;
    }

    @Override
    protected UserPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected Grid<User> getGrid() {
        return getViewComponent().list;
    }

    @Override
    protected void setGrid(Grid<User> grid) {
        getViewComponent().list = grid;
    }

    @Override
    protected Component getForm() {
        return getViewComponent().form;
    }

    @Override
    protected Button getAdd() {
        return getViewComponent().add;
    }

    @Override
    protected Button getCancel() {
        return getViewComponent().cancel;
    }

    @Override
    protected Button getDelete() {
        return getViewComponent().delete;
    }

    @Override
    protected Button getUpdate() {
        return getViewComponent().update;
    }

    @Override
    protected TextField getSearch() {
        return getViewComponent().search;
    }

    @Override
    protected Focusable getFirstFormField() {
        return getViewComponent().email;
    }

}
