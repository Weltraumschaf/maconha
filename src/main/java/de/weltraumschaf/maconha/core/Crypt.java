package de.weltraumschaf.maconha.core;

/**
 * This interface describes methods to hash and check passwords.
 */
public interface Crypt {
    /**
     * Hash a password.
     * <p>
     * Throws {@link IllegalArgumentException} if invalid salt is passed.
     * </p>
     *
     * @param password the password to hash
     * @param salt     the salt to hash with (perhaps generated using BCrypt.generateSalt)
     * @return the hashed password
     */
    String hashPassword(String password, String salt);

    /**
     * Generate a salt for use with the {@link #hashPassword(String, String)} method.
     *
     * @param log_rounds the log2 of the number of rounds of hashing to apply - the work
     *                   factor therefore increases as 2**log_rounds. Minimum 4, maximum 31.
     * @return an encoded salt value
     */
    String generateSalt(int log_rounds);

    /**
     * Check that a plaintext password matches a previously hashed one.
     *
     * @param plaintext the plaintext password to verify
     * @param hashed    the previously-hashed password
     * @return true if the passwords match, false otherwise
     */
    boolean checkPassword(String plaintext, String hashed);
}
