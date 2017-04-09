package de.weltraumschaf.maconha.frontend.admin;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 *
 */
final class AdminNavigator extends Navigator {
    AdminNavigator(final ComponentContainer container) {
        super(UI.getCurrent(), container);
    }
}
