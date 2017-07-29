package de.weltraumschaf.maconha.ui.navigation;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.spring.navigator.SpringNavigator;
import de.weltraumschaf.maconha.ui.view.dashboard.DashboardView;
import org.springframework.stereotype.Component;

/**
 * Governs view navigation of the app.
 */
@UIScope
@Component
public class NavigationManager extends SpringNavigator {

    /**
     * Find the view id (URI fragment) used for a given view class.
     *
     * @param viewClass the view class to find the id for
     * @return the URI fragment for the view
     */
    private String getViewId(final Class<? extends View> viewClass) {
        final SpringView springView = viewClass.getAnnotation(SpringView.class);

        if (springView == null) {
            throw new IllegalArgumentException("The target class must be a @SpringView");
        }

        return Conventions.deriveMappingForView(viewClass, springView);
    }

    /**
     * Navigate to the given view class.
     *
     * @param targetView the class of the target view, must be annotated using
     *                  {@link SpringView @SpringView}
     */
    public void navigateTo(final Class<? extends View> targetView) {
        navigateTo(getViewId(targetView));
    }

    public void navigateTo(final Class<? extends View> targetView, final Object parameter) {
        navigateTo(getViewId(targetView) + "/" + parameter.toString());
    }

    public void navigateToDefaultView() {
        // If the user wants a specific view, it's in the URL.
        // Otherwise admin goes to DashboardView and everybody else to
        // OrderListView
        if (!getState().isEmpty()) {
            return;
        }

        navigateTo(DashboardView.class);
    }

    /**
     * Update the parameter of the the current view without firing any
     * navigation events.
     *
     * @param parameter the new parameter to set, never <code>null</code>,
     *                  <code>""</code> to not use any parameter
     */
    public void updateViewParameter(final String parameter) {
        final String viewName = getViewId(getCurrentView().getClass());
        final String parameters;

        if (parameter == null) {
            parameters = "";
        } else {
            parameters = parameter;
        }

        updateNavigationState(new ViewChangeEvent(this, getCurrentView(), getCurrentView(), viewName, parameters));
    }

}
