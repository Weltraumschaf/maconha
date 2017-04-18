package de.weltraumschaf.maconha.frontend.admin.view.dashboard;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.weltraumschaf.maconha.frontend.admin.view.SubView;
import de.weltraumschaf.maconha.frontend.admin.view.buckets.BucketsView;

import javax.annotation.PostConstruct;

@UIScope
@SpringComponent
@SpringView(name = DashboardView.VIEW_NAME)
public final class DashboardView extends SubView {
    public static final String VIEW_NAME = "";
    public static final String TITLE = "Dashboard";
    private static final String TITLE_ID = "dashboard-title";

    DashboardView() {
        super(TITLE, TITLE_ID);
    }

    @Override
    protected void subInit() {
        root.addComponent(new Label("Hello, world!"));
    }

}
