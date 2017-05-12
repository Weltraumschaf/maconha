package de.weltraumschaf.maconha.frontend.admin.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This i the main view which has a menu on the left side and a content panel right side of the menu.
 */
@UIScope
@SpringComponent
@SpringViewDisplay
public final class MainView extends HorizontalLayout implements ViewDisplay {

    private final Panel content = new Panel();

    @Autowired
    public MainView(final AdminMenu menu) {
        super();
        setSizeFull();
        addComponent(menu);
        content.addStyleName(ValoTheme.PANEL_BORDERLESS);
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);
    }

    @Override
    public void showView(final View view) {
        content.setContent((Component) view);
    }
}
