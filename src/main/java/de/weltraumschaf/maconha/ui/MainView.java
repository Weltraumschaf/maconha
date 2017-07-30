package de.weltraumschaf.maconha.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.navigator.ViewLeaveAction;
import com.vaadin.spring.access.SecuredViewAccessControl;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.ui.navigation.NavigationManager;
import de.weltraumschaf.maconha.ui.view.buckets.BucketsView;
import de.weltraumschaf.maconha.ui.view.dashboard.DashboardView;
import de.weltraumschaf.maconha.ui.view.duplicates.DuplicatesView;
import de.weltraumschaf.maconha.ui.view.keywords.KeywordsView;
import de.weltraumschaf.maconha.ui.view.mediafiles.MediaFilesView;
import de.weltraumschaf.maconha.ui.view.scans.ScansView;
import de.weltraumschaf.maconha.ui.view.user.UserAdminView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * The main view containing the menu and the content area where actual views are
 * shown.
 * <p>
 * Created as a single View class because the logic is so simple that using a
 * pattern like MVP would add much overhead for little gain. If more complexity
 * is added to the class, you should consider splitting out a presenter.
 * </p>
 */
@UIScope
@SpringViewDisplay
public class MainView extends MainViewDesign implements ViewDisplay {

    private final Map<Class<? extends View>, Button> navigationButtons = new HashMap<>();
    private final NavigationManager navigationManager;
    private final SecuredViewAccessControl viewAccessControl;

    @Autowired
    public MainView(final NavigationManager navigationManager, final SecuredViewAccessControl viewAccessControl) {
        super();
        this.navigationManager = navigationManager;
        this.viewAccessControl = viewAccessControl;
    }

    @PostConstruct
    public void init() {
        attachNavigation(dashboard, DashboardView.class);
        attachNavigation(buckets, BucketsView.class);
        attachNavigation(mediaFiles, MediaFilesView.class);
        attachNavigation(keywords, KeywordsView.class);
        attachNavigation(scans, ScansView.class);
        attachNavigation(duplicates, DuplicatesView.class);
        attachNavigation(users, UserAdminView.class);

        logout.addClickListener(e -> logout());
    }

    /**
     * Makes clicking the given button navigate to the given view if the user has access to the view.
     * <p>
     * If the user does not have access to the view, hides the button.
     * </p>
     *
     * @param navigationButton the button to use for navigatio
     * @param targetView       the view to navigate to when the user clicks the button
     */
    private void attachNavigation(final Button navigationButton, final Class<? extends View> targetView) {
        boolean hasAccessToView = viewAccessControl.isAccessGranted(targetView);
        navigationButton.setVisible(hasAccessToView);

        if (hasAccessToView) {
            navigationButtons.put(targetView, navigationButton);
            navigationButton.addClickListener(e -> navigationManager.navigateTo(targetView));
        }
    }

    @Override
    public void showView(final View view) {
        content.removeAllComponents();
        content.addComponent(view.getViewComponent());

        navigationButtons.forEach((viewClass, button) ->
            button.setStyleName("selected", viewClass == view.getClass()));

        final Button menuItem = navigationButtons.get(view.getClass());
        String viewName = "";

        if (menuItem != null) {
            viewName = menuItem.getCaption();
        }

        activeViewName.setValue(viewName);
    }

    /**
     * Logs the user out after ensuring the currently open view has no unsaved changes.
     */
    public void logout() {
        final ViewLeaveAction doLogout = () -> {
            UI ui = getUI();
            ui.getSession().getSession().invalidate();
            ui.getPage().reload();
        };

        navigationManager.runAfterLeaveConfirmation(doLogout);
    }

}
