package de.weltraumschaf.maconha.job;

/**
 * Describes a property to configure a {@link Configurable}.
 */
interface Property {

    /**
     * The bean name of the property.
     *
     * @return never {@code null} nor empty
     */
    String getBeanName();
}
