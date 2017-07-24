package de.weltraumschaf.maconha.backend.model.entity;

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
public final class User extends BaseEntity {

    @NotEmpty
    @Size(min = 1, max = 256)
    @Column(nullable = false, unique = true)
    private String name;

    @NotEmpty
    @Size(min = 1, max = 256)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean admin;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(final boolean admin) {
        this.admin = admin;
    }

    @Override
    public final boolean equals(final Object o) {
        if (!(o instanceof User)) {
            return false;
        }

        final User user = (User) o;
        return admin == user.admin &&
            Objects.equals(name, user.name) &&
            Objects.equals(password, user.password);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(name, password, admin);
    }

    @Override
    public final String toString() {
        return "User{" +
            "name='" + name + '\'' +
            ", password='****'" + 
            ", admin=" + admin +
            '}';
    }
}
