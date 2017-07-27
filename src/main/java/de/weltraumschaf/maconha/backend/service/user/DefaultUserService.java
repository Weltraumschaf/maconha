package de.weltraumschaf.maconha.backend.service.user;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.app.MaconhaConfiguration;
import de.weltraumschaf.maconha.backend.model.Role;
import de.weltraumschaf.maconha.backend.model.entity.User;
import de.weltraumschaf.maconha.backend.repo.UserRepo;
import de.weltraumschaf.maconha.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Default implementation.
 */
@Service
final class DefaultUserService implements UserService {

    private final UserRepo users;
    private final MaconhaConfiguration config;
    private final PasswordEncoder crypt;

    @Lazy
    @Autowired
    DefaultUserService(final UserRepo users, final MaconhaConfiguration config, final PasswordEncoder crypt) {
        super();
        this.users = users;
        this.config = config;
        this.crypt = crypt;
    }

    @Override
    public boolean isThereNoAdminUser() {
        return users.findByRole(Role.ADMIN).isEmpty();
    }

    @Override
    public User createUnprivileged(final String name, final String password) {
        final User unprivileged = createUser(name, password);

        unprivileged.setRole(Role.USER);
        users.save(unprivileged);

        return unprivileged;
    }

    @Override
    public User createAdmin(final String name, final String password) {
        final User admin = createUser(name, password);

        admin.setRole(Role.ADMIN);
        users.save(admin);

        return admin;
    }

    private User createUser(final String name, final String password) {
        Validate.notEmpty(name, "name");
        Validate.notEmpty(password, "password");
        final User user = new User();

        user.setName(name);
        user.setPassword(crypt.encode(password));

        return user;
    }

    @Override
    public User authenticate(final String name, final String password) {
        Validate.notEmpty(name, "name");
        Validate.notEmpty(password, "password");
        final User user = users.findByName(name);

        if (null == user) {
            throw new AuthenticationFailed("No such user with name %s!", name);
        }

        if (!crypt.matches(password, user.getPassword())) {
            throw new AuthenticationFailed("Authentication failed for user %s!", user.getName());
        }

        return user;
    }
}
