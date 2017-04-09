package de.weltraumschaf.maconha.frontend.admin.view;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 */
final class AdminMenu extends CustomComponent {

    public static final String ID = "dashboard-menu";

    AdminMenu() {
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
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");

        for (final AdminViewType view : AdminViewType.values()) {
            final Component menuItemComponent = new ValoMenuItemButton(view);
            menuItemsLayout.addComponent(menuItemComponent);
        }

        return menuItemsLayout;
    }

    private final class ValoMenuItemButton extends Button {

        private final AdminViewType view;

        ValoMenuItemButton(final AdminViewType view) {
            super();
            this.view = view;
            setPrimaryStyleName("valo-menu-item");
            setIcon(view.getIcon());
            setCaption(view.getViewName().substring(0, 1).toUpperCase() + view.getViewName().substring(1));
            addClickListener((ClickListener) event -> UI.getCurrent().getNavigator().navigateTo(view.getViewName()));
        }
    }
}
