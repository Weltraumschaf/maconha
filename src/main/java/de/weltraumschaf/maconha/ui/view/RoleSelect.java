package de.weltraumschaf.maconha.ui.view;

import com.vaadin.ui.ComboBox;
import de.weltraumschaf.maconha.backend.model.Role;

public class RoleSelect extends ComboBox<String> {

    public RoleSelect() {
        super();
        setCaption("Role");
        setEmptySelectionAllowed(false);
        setItems(Role.getAllRoles());
        setTextInputAllowed(false);
    }
}
