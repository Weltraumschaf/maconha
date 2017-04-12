package de.weltraumschaf.maconha.frontend.admin.view.dashboard;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.weltraumschaf.maconha.frontend.admin.view.buckets.BucketsView;

import javax.annotation.PostConstruct;

@UIScope
@SpringComponent
@SpringView(name = DashboardView.VIEW_NAME)
public final class DashboardView extends Panel implements View {
    public static final String VIEW_NAME = "";
    private static final String TITLE_ID = "dashboard-title";

    private final Label titleLabel = new Label("Dashboard");
    private final VerticalLayout root = new VerticalLayout();

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method.
    }

    @PostConstruct
    public void init() {
        root.setSizeFull();
        root.setSpacing(false);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);
        root.addComponent(buildHeader());
    }

    private Component buildHeader() {
        final HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");

        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }
}
