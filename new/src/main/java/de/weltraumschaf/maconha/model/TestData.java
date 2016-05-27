package de.weltraumschaf.maconha.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 */
public final class TestData {

    public Collection<Media> generate(final int count) {
        final Collection<Media> data = new ArrayList<>();

        for (int i = 0; i < count; ++i) {
            final Media media = new Media();
            media.setId(i);
            media.setType(Media.Type.VIDEO);
            media.setTitle("title " + String.valueOf(i));
            media.setFilename("/tiele_" + String.valueOf(i));
            data.add(media);
        }

        return data;
    }
}
