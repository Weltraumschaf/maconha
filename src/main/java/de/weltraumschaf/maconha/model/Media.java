package de.weltraumschaf.maconha.model;

import de.weltraumschaf.maconha.core.FileExtension;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType type = MediaType.OTHER;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileExtension format = FileExtension.NONE;

    @NotEmpty
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String title = "";

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(iso = ISO.DATE_TIME)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime lastImported = new LocalDateTime();

    // Fixme Optional should be false.
    @OneToOne(optional = true ,cascade = {CascadeType.ALL})
    @JoinColumn(name = "originFile_id", referencedColumnName = "id", insertable = true, updatable = true)
    private OriginFile originFile;

    public int getId() {
        return id;
    }

    public Media setId(final int id) {
        this.id = id;
        return this;
    }

    public MediaType getType() {
        return type;
    }

    public Media setType(final MediaType type) {
        this.type = type;
        return this;
    }

    public FileExtension getFormat() {
        return format;
    }

    public Media setFormat(FileExtension format) {
        this.format = format;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Media setTitle(final String title) {
        this.title = title;
        return this;
    }

    public LocalDateTime getLastImported() {
        return lastImported;
    }

    public Media setLastImported(LocalDateTime lastImported) {
        this.lastImported = lastImported;
        return this;
    }

    public OriginFile getOriginFile() {
        return originFile;
    }

    public Media setOriginFile(OriginFile originFile) {
        this.originFile = originFile;
        return this;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, type, format, title, lastImported, originFile);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (!(obj instanceof Media)) {
            return false;
        }

        final Media other = (Media) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(type, other.type)
            && Objects.equals(format, other.format)
            && Objects.equals(title, other.title)
            && Objects.equals(lastImported, other.lastImported)
            && Objects.equals(originFile, other.originFile);
    }

    @Override
    public final String toString() {
        return "Media{"
            + "id=" + id + ", "
            + "type=" + type + ", "
            + "format=" + format + ", "
            + "title=" + title + ", "
            + "lastImported=" + lastImported + ", "
            + "originFile=" + originFile
            + '}';
    }

    public static enum MediaType {

        VIDEO, AUDIO, OTHER;
    };
}
