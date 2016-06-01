package de.weltraumschaf.maconha.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * Entity which represent imported media.
 * <p>
 * Must not be {@code final} for frameworks sake.
 * </p>
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType type = MediaType.OTHER;

    @NotEmpty
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String title = "";

    @NotEmpty
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String filename = "";

    @DateTimeFormat(iso = ISO.DATE_TIME)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime lastIndexed;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(final MediaType type) {
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

    public LocalDateTime getLastIndexed() {
        return lastIndexed;
    }

    public void setLastIndexed(final LocalDateTime lastIndexed) {
        this.lastIndexed = lastIndexed;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, type, title, filename, lastIndexed);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (!(obj instanceof Media)) {
            return false;
        }

        final Media other = (Media) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(type, other.type)
            && Objects.equals(title, other.title)
            && Objects.equals(filename, other.filename)
            && Objects.equals(lastIndexed, other.lastIndexed);
    }

    @Override
    public final String toString() {
        return "Media{"
            + "id=" + id + ", "
            + "type=" + type + ", "
            + "title=" + title + ", "
            + "filename=" + filename + ", "
            + "lastIndexed=" + lastIndexed
            + '}';
    }

    public static enum MediaType {

        VIDEO, AUDIO, OTHER;
    };
}
