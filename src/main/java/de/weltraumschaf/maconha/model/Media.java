package de.weltraumschaf.maconha.model;

import java.util.Objects;

/**
 */
public final class Media {

    private final long id;
    private final Type type;
    private final String title;
    private final String path;

    public Media(final long id, final Type type, final String title, final String path) {
        super();
        this.id = id;
        this.type = type;
        this.title = title;
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, title, path);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Media)) {
            return false;
        }

        final Media other = (Media) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(type, other.type)
            && Objects.equals(title, other.title)
            && Objects.equals(path, other.path);
    }

    @Override
    public String toString() {
        return "Media{" + "id=" + id + ", type=" + type + ", title=" + title + ", path=" + path + '}';
    }

    public static enum Type {

        MOVIE, SERIES, MUSIC;
    };
}
