package de.weltraumschaf.maconha.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 */
public final class TestData {

    public Collection<Media> generate(final int count) {
        final Collection<Media> data = new ArrayList<>();

        for (int i = 0 ; i < count; ++i) {
            data.add(new Media(i, Media.Type.MOVIE, "title " + String.valueOf(i), "/tiele_" + String.valueOf(i)));
        }

        return data;
    }
}
