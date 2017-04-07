package de.weltraumschaf.maconha;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import de.weltraumschaf.maconha.view.LoginView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

/**
 * https://github.com/mstahv/spring-data-vaadin-crud
 */
@Theme("valo")
@SpringUI(path = "/admin")
@Title("Maconha - Admin")
public final class AdminUi extends UI implements EventBusListener<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUi.class);

    @Autowired
    private EventBus.UIEventBus events;

    @Override
    protected void init(final VaadinRequest request) {
        events.subscribe(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();
    }

    private void updateContent() {
        setContent(new LoginView(events));
        addStyleName("loginview");
    }

    @Override
    public void onEvent(final org.vaadin.spring.events.Event<String> event) {
        LOGGER.info("Login performed: {}", event.getPayload());
    }
}
