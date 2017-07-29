package de.weltraumschaf.maconha.ui;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import de.weltraumschaf.maconha.app.HasLogger;
import de.weltraumschaf.maconha.ui.navigation.NavigationManager;
import de.weltraumschaf.maconha.ui.view.AccessDeniedView;
import org.springframework.beans.factory.annotation.Autowired;

import static com.vaadin.shared.ui.ui.Transport.LONG_POLLING;

/**
 * This is the root of the administrative backend UI.
 */
@Theme("maconha")
@Title("Maconha - Admin")
@SpringUI(path = "/admin")
@Viewport("width=device-width,initial-scale=1.0,user-scalable=no")
@Push(transport = LONG_POLLING)
public final class AdminUi extends UI implements HasLogger {


    private final SpringViewProvider viewProvider;
    private final NavigationManager navigationManager;
    private final MainView mainView;

    @Autowired
    public AdminUi(final SpringViewProvider viewProvider, final NavigationManager navigationManager, final MainView mainView) {
        super();
        setErrorHandler(event -> {
            Throwable t = DefaultErrorHandler.findRelevantThrowable(event.getThrowable());
            logger().error("Error during request", t);
        });
        this.viewProvider = viewProvider;
        this.navigationManager = navigationManager;
        this.mainView = mainView;
    }

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
        setContent(mainView);
        navigationManager.navigateToDefaultView();
    }
}
