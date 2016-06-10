package de.weltraumschaf.maconha.model;

import java.io.Serializable;
import java.util.Collection;

/**
 */
abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    protected final <T> boolean sameAsFormer(final T oldValue, final T newValue) {
        return oldValue == null
            ? newValue == null
            : oldValue.equals(newValue);
    }

    protected final <T> boolean isAlreadyAdded(final Collection<T> oldValues, final T newValue) {
        return oldValues.contains(newValue);
    }

}
