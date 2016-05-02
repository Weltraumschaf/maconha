package de.weltraumschaf.maconha.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 */
@Entity
@Table(name = "Media")
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "type", nullable = false)
    private Type type;

    @NotEmpty
    @Size(min = 3, max = 255)
    @Column(name = "title", nullable = false)
    private String title;

    @NotEmpty
    @Size(min = 1, max = 255)
    @Column(name = "filename", nullable = false)
    private String filename;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(final String filename) {
        this.filename = filename;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, title, filename);
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
            && Objects.equals(filename, other.filename);
    }

    @Override
    public String toString() {
        return "Media{"
            + "id=" + id + ", "
            + "type=" + type + ", "
            + "title=" + title + ", "
            + "filename=" + filename
            + '}';
    }

    public static enum Type {

        MOVIE, SERIES, MUSIC;
    };
}
