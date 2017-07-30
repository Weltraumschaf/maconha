package de.weltraumschaf.maconha.backend.service.user;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.model.Role;
import de.weltraumschaf.maconha.backend.model.entity.User;
import de.weltraumschaf.maconha.backend.repo.UserRepo;
import de.weltraumschaf.maconha.backend.service.CrudService;
import de.weltraumschaf.maconha.backend.service.UserFriendlyDataException;
import de.weltraumschaf.maconha.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Default implementation.
 */
@Service
class DefaultUserService implements UserService, CrudService<User> {

    private static final String MODIFY_LOCKED_USER_NOT_PERMITTED = "User has been locked and cannot be modified or deleted";

    private final UserRepo users;
    private final PasswordEncoder crypt;

    @Autowired
    DefaultUserService(final UserRepo users, final PasswordEncoder crypt) {
        super();
        this.users = users;
        this.crypt = crypt;
    }

    @Override
    public boolean isThereNoAdminUser() {
        return users.findByRole(Role.ADMIN).isEmpty();
    }

    @Override
    public User createUnprivileged(final String name, final String password, final String email) {
        final User unprivileged = createUser(name, password, email);

        unprivileged.setRole(Role.USER);
        users.save(unprivileged);

        return unprivileged;
    }

    @Override
    public User createAdmin(final String name, final String password, final String email) {
        final User admin = createUser(name, password, email);

        admin.setRole(Role.ADMIN);
        users.save(admin);

        return admin;
    }

    private User createUser(final String name, final String password, final String email) {
        Validate.notEmpty(name, "name");
        Validate.notEmpty(password, "password");
        final User user = new User();

        // TODO add some validations:
        // not blank
        // name != password
        // email != password
        // valid email
        user.setName(name);
        user.setPassword(crypt.encode(password));
        user.setEmail(email);

        return user;
    }

    @Override
    public String encodePassword(final String plaintext) {
        return crypt.encode(plaintext);
    }

    @Override
    public UserRepo getRepository() {
        return users;
    }

    @Override
    public long countAnyMatching(final Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository().countByEmailLikeIgnoreCaseOrNameLikeIgnoreCase(repositoryFilter, repositoryFilter);
        } else {
            return getRepository().count();
        }

    }

    @Override
    public Page<User> findAnyMatching(final Optional<String> filter, final Pageable pageable) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository().findByEmailLikeIgnoreCaseOrNameLikeIgnoreCaseOrRoleLikeIgnoreCase(repositoryFilter,
                repositoryFilter, repositoryFilter, pageable);
        } else {
            return find(pageable);
        }
    }

    @Override
    public Page<User> find(final Pageable pageable) {
        return getRepository().findBy(pageable);
    }

    @Override
    @Transactional
    public User save(User entity) {
        throwIfUserLocked(entity.getId());
        return getRepository().save(entity);
    }

    @Override
    @Transactional
    public void delete(long userId) {
        throwIfUserLocked(userId);
        getRepository().delete(userId);
    }

    private void throwIfUserLocked(Long userId) {
        if (userId == null) {
            return;
        }

        User dbUser = getRepository().findOne(userId);
        if (dbUser.isLocked()) {
            throw new UserFriendlyDataException(MODIFY_LOCKED_USER_NOT_PERMITTED);
        }
    }
}
