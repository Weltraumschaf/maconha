package de.weltraumschaf.maconha.service;

import de.weltraumschaf.maconha.model.User;

/**
 *
 */
public interface UserService {
    boolean isThereNoAdminUser();
    User createUnprivileged(String name, String password);
    User createAdmin(String name, String password);
    User authenticate(String value, String value1);

    final class AuthenticationFailed extends RuntimeException {
        public AuthenticationFailed(final String message, final Object... args) {
            super(String.format(message, args));
        }
    }
}
