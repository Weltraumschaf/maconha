package de.weltraumschaf.maconha.backend.model.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String email;

    @NotNull
    @NotEmpty
    @Size(min = 4, max = 255)
    private String password;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String role;

    private boolean locked;

    public User() {
        // An empty constructor is needed for all beans
        super();
    }

    public User(String email, String name, String password, String role) {
        Objects.requireNonNull(email);
        Objects.requireNonNull(name);
        Objects.requireNonNull(password);
        Objects.requireNonNull(role);

        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public final boolean equals(final Object o) {
        if (!(o instanceof User)) {
            return false;
        }

        final User user = (User) o;
        return Objects.equals(name, user.name);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "User{" +
            "email='" + email + '\'' +
            ", password='***'" +
            ", name='" + name + '\'' +
            ", role='" + role + '\'' +
            ", locked=" + locked +
            '}';
    }
}
