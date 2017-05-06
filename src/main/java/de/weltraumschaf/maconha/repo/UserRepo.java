package de.weltraumschaf.maconha.repo;

import de.weltraumschaf.maconha.model.User;

import java.util.Collection;

/**
 *
 */
public interface UserRepo extends BaseRepo<User> {
    User findByName(String name);

    Collection<User> findByAdmin(boolean isAdmin);
}
