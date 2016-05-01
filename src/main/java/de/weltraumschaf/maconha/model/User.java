package de.weltraumschaf.maconha.model;

import java.util.Objects;

public final class User {

    private long id;

    private String username;

    private String address;

    private String email;

    public User() {
        id = 0;
    }

    public User(long id, String username, String address, String email) {
        this.id = id;
        this.username = username;
        this.address = address;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, address, email);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }

        final User other = (User) obj;
        return Objects.equals(id, other.id)
            && Objects.equals(username, other.username)
            && Objects.equals(address, other.address)
            && Objects.equals(email, other.email);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", address=" + address
            + ", email=" + email + "]";
    }

}
