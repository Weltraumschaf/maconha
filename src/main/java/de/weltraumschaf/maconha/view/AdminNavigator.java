package de.weltraumschaf.maconha.view;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 *
 */
public final class AdminNavigator extends Navigator {
    public AdminNavigator(final ComponentContainer container) {
        super(UI.getCurrent(), container);
    }
}
