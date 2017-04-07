package de.weltraumschaf.maconha;

import com.vaadin.server.WidgetsetInfo;

/**
 *
 */
public final class AppWidgetset implements WidgetsetInfo {

    public static final String NAME = "wsc77af946b966b8373a35d19f0624b738";

    @Override
    public String getWidgetsetName() {
        return NAME;
    }

    @Override
    public String getWidgetsetUrl() {
        return "https://ws.vaadin.com/" + NAME + "/" + NAME +".nocache.js";
    }

    @Override
    public boolean isCdn() {
        return false;
    }
}
