package de.weltraumschaf.maconha.core;

/**
 *
 */
public interface Crypt {
    String hashpw(String password, String salt);
    String gensalt(int log_rounds);
    boolean checkpw(String plaintext, String hashed);
}
