package de.weltraumschaf.maconha.frontend.admin.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.weltraumschaf.maconha.frontend.admin.view.buckets.BucketsView;
import de.weltraumschaf.maconha.frontend.admin.view.dashboard.DashboardView;
import de.weltraumschaf.maconha.frontend.admin.view.duplicates.DuplicatesView;
import de.weltraumschaf.maconha.frontend.admin.view.keywords.KeywordsView;
import de.weltraumschaf.maconha.frontend.admin.view.mediafiles.MediaFilesView;
import de.weltraumschaf.maconha.frontend.admin.view.scans.ScansView;
import de.weltraumschaf.maconha.model.Keyword;

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
        menuItemsLayout.addComponent(createNavigationButton(VaadinIcons.HOME, DashboardView.VIEW_NAME, DashboardView.TITLE));
        menuItemsLayout.addComponent(createNavigationButton(VaadinIcons.TABLE, BucketsView.VIEW_NAME, BucketsView.TITLE));
        menuItemsLayout.addComponent(createNavigationButton(VaadinIcons.TABLE, MediaFilesView.VIEW_NAME, MediaFilesView.TITLE));
        menuItemsLayout.addComponent(createNavigationButton(VaadinIcons.TABLE, KeywordsView.VIEW_NAME, KeywordsView.TITLE));
        menuItemsLayout.addComponent(createNavigationButton(VaadinIcons.TABLE, ScansView.VIEW_NAME, ScansView.TITLE));
        menuItemsLayout.addComponent(createNavigationButton(VaadinIcons.TABLE, DuplicatesView.VIEW_NAME, DuplicatesView.TITLE));
        return menuItemsLayout;
    }

    private Button createNavigationButton(final Resource icon, final String viewName, final String caption) {
        final MenuItemButton button = new MenuItemButton(icon, caption);
        button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
        return button;
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
