package de.weltraumschaf.maconha.backend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entity which represent an indexed keyword.
 * <p>
 * Must not be {@code final} for frameworks sake.
 * </p>
 */
@Entity
@Table(indexes = {@Index(name = "idx_literal", columnList = "literal")})
public class Keyword extends BaseEntity {

    @NotEmpty
    @Size(min = 1, max = 255)
    @Column(unique = true, nullable = false)
    private String literal;

    @JsonIgnore // Prevent endless recursion on serialization.
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @SuppressWarnings("FieldMayBeFinal") // XXX Must not be final?
    @JoinTable(name = "Keyword_MediaFile", joinColumns = {@JoinColumn(name = "keyword_id")}, inverseJoinColumns = {@JoinColumn(name = "media_id")})
    private Set<MediaFile> mediaFiles = new HashSet<>();

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
        // Id is not considered because it is different for new objects and persisted ones.
        // MediaFiles not considered because this would lead to endless recursion.
        return Objects.hash(literal);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (!(obj instanceof Keyword)) {
            return false;
        }

        final Keyword other = (Keyword) obj;
        // Id is not considered because it is different for new objects and persisted ones.
        // MediaFiles not considered because this would lead to endless recursion.
        return Objects.equals(literal, other.literal);
    }

    @Override
    public String toString() {
        return "Keyword{"
            + "id=" + getId() + ", "
            + "literal=" + literal + ", "
            + "mediaFiles=" + mediaFiles
            + '}';
    }

}
