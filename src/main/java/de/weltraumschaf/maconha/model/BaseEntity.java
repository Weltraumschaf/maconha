package de.weltraumschaf.maconha.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * Shared functionality for entities.
 */
abstract class BaseEntity implements Serializable {

    // For the sake of the JPA implementation.
    private static final long serialVersionUID = 1L;

    /**
     * Compares if the two given values are logical equal.
     * <p>
     *     This method also considers null values.
     * </p>
     * @param oldValue may be {@code null}
     * @param newValue may be {@code null}
     * @param <T> compared types
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
     * @param newValue may be {@code null}
     * @param <T> compared types
     * @return {@code true} if new value is contained in the collection or is {@code null}, else {@code false}
     */
    final <T> boolean isAlreadyAdded(final Collection<T> oldValues, final T newValue) {
        return oldValues.contains(newValue);
    }

}
