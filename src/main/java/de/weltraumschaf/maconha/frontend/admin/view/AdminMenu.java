package de.weltraumschaf.maconha.frontend.admin.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@UIScope
@SpringComponent
public final class AdminMenu extends CustomComponent {

    public static final String ID = "dashboard-menu";

    public AdminMenu() {
        super();
        setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();
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
        menuContent.addComponent(buildMenuItems());

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

    private Component buildMenuItems() {
        final CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");
        menuItemsLayout.addComponent(new MenuItemButton(VaadinIcons.HOME, "Dashboard"));
        menuItemsLayout.addComponent(new MenuItemButton(VaadinIcons.TABLE, "Buckets"));
        return menuItemsLayout;
    }

    private final class MenuItemButton extends Button {

        MenuItemButton(final Resource icon, final String caption) {
            super();
            setPrimaryStyleName("valo-menu-item");
            setIcon(icon);
            setCaption(caption);
        }
    }
}
