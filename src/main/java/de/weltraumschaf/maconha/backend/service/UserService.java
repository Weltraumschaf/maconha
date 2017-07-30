package de.weltraumschaf.maconha.backend.service;

import de.weltraumschaf.maconha.backend.model.entity.User;

/**
 * Service to manage users.
 */
public interface UserService extends CrudService<User> {
    /**
     * If there is at least one user with administrator role.
     *
     * @return {@code ture} if there is one, else {@code false}
     */
    boolean isThereNoAdminUser();

    /**
     * Creates an unprivileged user.
     *
     * @param name     must not be {@code null} or blank
     * @param password must not be {@code null} or blank or same as name
     * @param email    must not be {@code null} or blank
     * @return the new created user, never {@code null}
     */
    User createUnprivileged(String name, String password, String email);

    /**
     * Creates an privileged user.
     *
     * @param name     must not be {@code null} or blank
     * @param password must not be {@code null} or blank or same as name
     * @param email    must not be {@code null} or blank
     * @return the new created user, never {@code null}
     */
    User createAdmin(String name, String password, String email);

    String encodePassword(String plaintext);

}
