package de.weltraumschaf.maconha.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity which represent an indexed keyword.
 * <p>
 * Must not be {@code final} for frameworks sake.
 * </p>
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
public class Keyword implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 1, max = 255)
    private String literal;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
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
        return "Keyword{" + "id=" + id + ", literal=" + literal + '}';
    }

}
