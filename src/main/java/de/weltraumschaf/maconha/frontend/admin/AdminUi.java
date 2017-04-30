package de.weltraumschaf.maconha.frontend.admin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import de.weltraumschaf.maconha.model.User;
import de.weltraumschaf.maconha.frontend.admin.view.LoginView;
import de.weltraumschaf.maconha.frontend.admin.view.MainView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

/**
 * This is the root of the administrative backend UI.
 */
@Theme("valo")
@Title("Maconha - Admin")
@SpringUI(path = "/admin")
public final class AdminUi extends UI implements EventBusListener<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUi.class);

    private final EventBus.UIEventBus events;
    private final MainView main;
    private final LoginView login;

    @Autowired
    public AdminUi(final EventBus.UIEventBus events, final MainView main, final LoginView login) {
        super();
        this.events = events;
        this.main = main;
        this.login = login;
    }

    @Override
    protected void init(final VaadinRequest request) {
        events.subscribe(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();
    }

    private void updateContent() {
        final User user = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());

        if (null != user && user.isAdmin()) {
            setContent(main);
            removeStyleName("loginview");
            getNavigator().navigateTo(getNavigator().getState());
        } else {
            setContent(login);
            addStyleName("loginview");
        }
    }

    @Override
    public void onEvent(final org.vaadin.spring.events.Event<User> event) {
        final User user = event.getPayload();
        LOGGER.info("Login performed: {}", user);
        VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
        updateContent();
    }
}
