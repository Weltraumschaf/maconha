package de.weltraumschaf.maconha.ui.view.user;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import de.weltraumschaf.maconha.backend.model.entity.User;
import de.weltraumschaf.maconha.backend.service.UserService;
import de.weltraumschaf.maconha.ui.navigation.NavigationManager;
import de.weltraumschaf.maconha.ui.view.BaseCrudPresenter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@SpringComponent
@ViewScope
public class UserAdminPresenter extends BaseCrudPresenter<User, UserService, UserAdminView>
    implements Serializable {

    @Autowired
    public UserAdminPresenter(UserAdminDataProvider userAdminDataProvider, NavigationManager navigationManager,
                              UserService service) {
        super(navigationManager, service, userAdminDataProvider);
    }

    public String encodePassword(String value) {
        return getService().encodePassword(value);
    }

    @Override
    protected void editItem(User item) {
        super.editItem(item);
        getView().setPasswordRequired(item.isNew());
    }
}