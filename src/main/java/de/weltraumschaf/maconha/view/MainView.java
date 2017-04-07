package de.weltraumschaf.maconha.view;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.spring.events.EventBus;

/**
 *
 */
public final class MainView extends HorizontalLayout {
    public MainView(final EventBus.UIEventBus events) {
        super();
        setSizeFull();
        addStyleName("mainview");

        addComponent(new AdminMenu(events));

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new AdminNavigator(content);
    }
}
