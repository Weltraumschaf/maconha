package de.weltraumschaf.maconha.frontend.admin.view.dashboard;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import de.weltraumschaf.maconha.frontend.admin.view.buckets.BucketsView;

import javax.annotation.PostConstruct;

@UIScope
@SpringComponent
@SpringView(name = DashboardView.VIEW_NAME)
public final class DashboardView extends Panel implements View {
    public static final String VIEW_NAME = "dashboard";

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method.
    }

    @PostConstruct
    public void init() {
        setContent(new Label("Hello, world!"));
    }
}
