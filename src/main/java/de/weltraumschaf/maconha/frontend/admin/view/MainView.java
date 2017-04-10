package de.weltraumschaf.maconha.frontend.admin.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@UIScope
@SpringComponent
@SpringViewDisplay
public final class MainView extends HorizontalLayout implements ViewDisplay {

    private final ComponentContainer content = new CssLayout();

    @Autowired
    public MainView(final AdminMenu menu) {
        super();
        setSizeFull();
        addStyleName("mainview");
        addComponent(menu);
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);
    }

    @Override
    public void showView(final View view) {
        content.removeAllComponents();
        content.addComponent((Component) view);
    }
}
