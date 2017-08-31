package de.weltraumschaf.maconha.ui.components;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;

/**
 *
 */
public final class PrimaryButton extends Button {
    public PrimaryButton(final String caption) {
        super(caption);
        setStyleName("primary default");
        setClickShortcut(ShortcutAction.KeyCode.ENTER, null);
    }
}
