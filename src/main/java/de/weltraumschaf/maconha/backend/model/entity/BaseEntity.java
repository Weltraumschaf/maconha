package de.weltraumschaf.maconha.backend.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Shared functionality for entities.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    // For the sake of the JPA implementation.
    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of an entity, which is not set for newly constructed objects.
     * <p>
     * This value is set after the object was persisted. So this value changes during the life time of an entity.
     * So it must not be considered in {@link #equals(Object)} and {@link #hashCode()}. Each entity must define
     * by it self what means an equal object or not (logical key).
     * </p>
     */
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    public boolean isNew() {
        return id == null;
    }

    // Must not be final for Vaadin bean property access.
    public Long getId() {
        return id;
    }

    /**
     * Compares if the two given values are logical equal.
     * <p>
     * This method also considers null values.
     * </p>
     *
     * @param oldValue may be {@code null}
     * @param newValue may be {@code null}
     * @param <T>      compared types
     * @return {@code true} if both values are logically equal or both {@code null}, else {@code false}
     */
    final <T> boolean sameAsFormer(final T oldValue, final T newValue) {
        return oldValue == null
            ? newValue == null
            : oldValue.equals(newValue);
    }

    /**
     * Checks if a given new value is already in a given collection.
     *
     * @param oldValues must not be {@code null}
     * @param newValue  may be {@code null}
     * @param <T>       compared types
     * @return {@code true} if new value is contained in the collection or is {@code null}, else {@code false}
     */
    final <T> boolean isAlreadyAdded(final Collection<T> oldValues, final T newValue) {
        return oldValues.contains(newValue);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }

        return 31 + id.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (id == null) {
            // New entities are only equal if the instance if the same
            return super.equals(other);
        }

        if (this == other) {
            return true;
        }

        if (!(other instanceof BaseEntity)) {
            return false;
        }

        return id.equals(((BaseEntity) other).id);
    }
}
