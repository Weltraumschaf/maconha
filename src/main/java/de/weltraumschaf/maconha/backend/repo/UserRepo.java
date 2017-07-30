package de.weltraumschaf.maconha.backend.repo;

import de.weltraumschaf.maconha.backend.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 *
 */
public interface UserRepo extends BaseRepo<User> {
    User findByName(String name);

    Collection<User> findByRole(String role);

    User findByEmail(String email);

    Page<User> findBy(Pageable pageable);

    Page<User> findByEmailLikeIgnoreCaseOrNameLikeIgnoreCaseOrRoleLikeIgnoreCase(String emailLike, String nameLike,
                                                                                 String roleLike, Pageable pageable);

    long countByEmailLikeIgnoreCaseOrNameLikeIgnoreCase(String emailLike, String nameLike);
}
