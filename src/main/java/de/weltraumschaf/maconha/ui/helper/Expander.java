package de.weltraumschaf.maconha.ui.helper;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 *
 */
public final class Expander {
    public static void addAndExpand(final VerticalLayout container, final Component... componentsToExpand) {
        if (container.getHeight() < 0) {
            // Make full height if no other size is set
            container.setHeight(100, Sizeable.Unit.PERCENTAGE);
        }

        for (final Component component : componentsToExpand) {
            if (component.getParent() != container) {
                container.addComponent(component);
            }

            container.setExpandRatio(component, 1);
            component.setHeight(100, Sizeable.Unit.PERCENTAGE);
        }
    }
}
