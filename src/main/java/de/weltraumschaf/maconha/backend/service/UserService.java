package de.weltraumschaf.maconha.backend.service;

import de.weltraumschaf.maconha.backend.model.entity.User;

/**
 * Service to manage users.
 */
public interface UserService {
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

    /**
     * Authenticates a user identified by name and password.
     * <p>
     * Throws an {@link AuthenticationFailed} if the given credentials can't be authenticated.
     * </p>
     *
     * @param name must not be {@code null} or blank
     * @param password in plaintext, must not be {@code null} or blank
     * @return authenticated user, never {@code null}
     */
    User authenticate(String name, String password);

    /**
     * Indicates a failed authentication.
     */
    final class AuthenticationFailed extends RuntimeException {
        public AuthenticationFailed(final String message, final Object... args) {
            super(String.format(message, args));
        }
    }
}
