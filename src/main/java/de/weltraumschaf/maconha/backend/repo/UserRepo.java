package de.weltraumschaf.maconha.backend.repo;

import de.weltraumschaf.maconha.backend.model.User;

import java.util.Collection;

/**
 *
 */
public interface UserRepo extends BaseRepo<User> {
    User findByName(String name);

    Collection<User> findByAdmin(boolean isAdmin);
}
