package de.weltraumschaf.maconha.frontend.admin.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import javax.annotation.PostConstruct;

/**
 *
 */
public abstract class SubView extends Panel implements View {

    protected final VerticalLayout root = new VerticalLayout();
    private final Label titleLabel;
    private final String titleId;

    public SubView(final String title, final String titleId) {
        super();
        this.titleLabel = new Label(title);
        this.titleId= titleId;
    }

    @Override
    public final void enter(final ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method.
    }

    @PostConstruct
    public final void init() {
        root.setSizeFull();
        root.setSpacing(false);
        setContent(root);
        Responsive.makeResponsive(root);
        root.addComponent(buildHeader());
        subInit();
    }

    private Component buildHeader() {
        final HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");

        titleLabel.setId(titleId);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    }

    protected abstract void subInit();
}
