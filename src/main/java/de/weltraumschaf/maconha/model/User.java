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
public final class User extends BaseEntity {

    @NotEmpty
    @Size(min = 1, max = 256)
    @Column(nullable = false, unique = true)
    private String name;

    @NotEmpty
    @Size(min = 1, max = 256)
    @Column(nullable = false)
    private String password;

    @NotEmpty
    @Size(min = 1, max = 256)
    @Column(nullable = false)
    private String salt;

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

    public String getSalt() {
        return salt;
    }

    public void setSalt(final String salt) {
        this.salt = salt;
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
            Objects.equals(password, user.password) &&
            Objects.equals(salt, user.salt);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(name, password, salt, admin);
    }

    @Override
    public final String toString() {
        return "User{" +
            "name='" + name + '\'' +
            ", password='" + password + '\'' +
            ", salt='" + salt + '\'' +
            ", admin=" + admin +
            '}';
    }
}
