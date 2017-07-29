package de.weltraumschaf.maconha.app;

import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

/**
 * Configures the VaadinService instance that serves the app through a servlet.
 * <p>
 * Uses a bootstrap listener to modify the bootstrap HTML page and include icons
 * for home screen for mobile devices.
 * </p>
 */
@Component
public class ApplicationInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(final ServiceInitEvent serviceInitEvent) {
        final VaadinService service = serviceInitEvent.getSource();

        service.addSessionInitListener(event -> {
            event.getSession().addBootstrapListener(new IconBootstrapListener());
        });
    }
}
