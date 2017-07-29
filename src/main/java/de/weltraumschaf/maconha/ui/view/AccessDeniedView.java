package de.weltraumschaf.maconha.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import org.vaadin.spring.annotation.PrototypeScope;

@SpringComponent
@PrototypeScope
public class AccessDeniedView extends AccessDeniedDesign implements View {

    @Override
    public void enter(ViewChangeEvent event) {
        // Nothing to do, just show the view
    }

}
