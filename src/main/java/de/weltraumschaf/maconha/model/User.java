package de.weltraumschaf.maconha.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Entity which represent an user.
 * <p>
 * Must not be {@code final} for frameworks sake.
 * </p>
 */
@Entity
@SuppressWarnings("PersistenceUnitPresent")
public final class User extends BaseEntity {

    @NotEmpty
    @Size(min = 1, max = 256)
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean admin;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(final boolean admin) {
        this.admin = admin;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof User)) {
            return false;
        }

        final User user = (User) o;
        return admin == user.admin &&
            Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, admin);
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + getId() + ", " +
            "name='" + name + '\'' +
            ", admin=" + admin +
            '}';
    }
}
