package de.weltraumschaf.maconha.model;

import java.util.Objects;

/**
 *
 */
public final class User {
    private final String name;
    private final boolean admin;

    public User(final String name, final boolean admin) {
        super();
        this.name = name;
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public boolean isAdmin() {
        return admin;
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
            "name='" + name + '\'' +
            ", admin=" + admin +
            '}';
    }
}
