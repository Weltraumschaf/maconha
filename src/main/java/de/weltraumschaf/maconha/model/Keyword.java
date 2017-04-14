package de.weltraumschaf.maconha.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity which represent an indexed keyword.
 * <p>
 * Must not be {@code final} for frameworks sake.
 * </p>
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
public class Keyword extends BaseEntity {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @Size(min = 1, max = 255)
    @Column(unique = true, nullable = false)
    private String literal;

    @JsonIgnore
    @ManyToMany
    @SuppressWarnings("FieldMayBeFinal") // XXX Must not be final?
    @JoinTable(name = "Keyword_MediaFile", joinColumns = {@JoinColumn(name = "keyword_id")}, inverseJoinColumns = {@JoinColumn(name = "media_id")})
    private Set<MediaFile> mediaFiles = new HashSet<>();

    public int getId() {
        return id;
    }

    public Keyword setId(final int id) {
        this.id = id;
        return this;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(final String literal) {
        this.literal = literal;
    }

    public Set<MediaFile> getMediaFiles() {
        return mediaFiles;
    }

    public void addMedias(final MediaFile mediaFile) {
        if (isAlreadyAdded(mediaFile)) {
            return;
        }

        mediaFiles.add(mediaFile);
        mediaFile.addKeyword(this);
    }

    private boolean isAlreadyAdded(final MediaFile newMediaFile) {
        return isAlreadyAdded(mediaFiles, newMediaFile);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id, literal);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (!(obj instanceof Keyword)) {
            return false;
        }

        final Keyword other = (Keyword) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(literal, other.literal);
    }

    @Override
    public String toString() {
        return "Keyword{"
            + "id=" + id + ", "
            + "literal=" + literal + ", "
            + "mediaFiles=" + mediaFiles
            + '}';
    }

}
