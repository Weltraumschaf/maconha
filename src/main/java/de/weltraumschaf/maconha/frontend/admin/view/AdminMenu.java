package de.weltraumschaf.maconha.frontend.admin.view;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.spring.events.EventBus;

/**
 *
 */
public final class AdminMenu extends CustomComponent {

    public static final String ID = "dashboard-menu";
    private final EventBus.UIEventBus events;

    public AdminMenu(final EventBus.UIEventBus events) {
        super();
        this.events = events;
        setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();

        // There's only one DashboardMenu per UI so this doesn't need to be
        // unregistered from the UI-scoped event bus.
        events.subscribe(this);

        setCompositionRoot(buildContent());
    }

    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());

        return menuContent;
    }

    private Component buildTitle() {
        final Label logo = new Label("Maconha <strong>Admin</strong>",  ContentMode.HTML);
        logo.setSizeUndefined();
        final HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }
}
