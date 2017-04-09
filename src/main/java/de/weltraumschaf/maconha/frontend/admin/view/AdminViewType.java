package de.weltraumschaf.maconha.frontend.admin.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;
import de.weltraumschaf.maconha.frontend.admin.view.buckets.BucketsView;
import de.weltraumschaf.maconha.frontend.admin.view.dashboard.DashboardView;

/**
 *
 */
enum AdminViewType {
    DASHBOARD("dashboard", DashboardView.class, VaadinIcons.HOME, true),
    BUCKETS("buckets", BucketsView.class, VaadinIcons.TABLE, false);

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

    AdminViewType(final String viewName, final Class<? extends View> viewClass, final Resource icon, final boolean stateful) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    boolean isStateful() {
        return stateful;
    }

    String getViewName() {
        return viewName;
    }

    Class<? extends View> getViewClass() {
        return viewClass;
    }

    Resource getIcon() {
        return icon;
    }

    static AdminViewType getByViewName(final String viewName) {
        AdminViewType result = null;

        for (AdminViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }

        return result;
    }
}
