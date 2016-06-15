package de.weltraumschaf.maconha.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(joinColumns = {@JoinColumn(name = "keyword_id")}, inverseJoinColumns = {@JoinColumn(name = "media_id")})
    @SuppressWarnings("FieldMayBeFinal")
    private Set<Media> medias = new HashSet<>();

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

    public Keyword setLiteral(final String literal) {
        this.literal = literal;
        return this;
    }

    public Set<Media> getMedias() {
        return medias;
    }

    public Keyword addMedias(final Media media) {
        if (isAlreadyAdded(media)) {
            return this;
        }

        medias.add(media);
        media.addKeyword(this);
        return this;
    }

    private boolean isAlreadyAdded(final Media newMedia) {
        return isAlreadyAdded(medias, newMedia);
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
            + "medias=" + medias
            + '}';
    }

}
