package de.weltraumschaf.maconha.service.user;

import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.core.BCrypt;
import de.weltraumschaf.maconha.model.User;
import de.weltraumschaf.maconha.repo.UserRepo;
import de.weltraumschaf.maconha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
final class DefaultUserService implements UserService {

    private final UserRepo users;
    private final MaconhaConfiguration config;

    @Lazy
    @Autowired
    DefaultUserService(final UserRepo users, final MaconhaConfiguration config) {
        super();
        this.users = users;
        this.config = config;
    }

    @Override
    public boolean isThereNoAdminUser() {
        return users.findByAdmin(true).isEmpty();
    }

    @Override
    public User createUnprivileged(final String name, final String password) {
        final User unprivileged = createUser(name, password);

        unprivileged.setAdmin(false);
        users.save(unprivileged);

        return unprivileged;
    }

    @Override
    public User createAdmin(final String name, final String password) {
        final User admin = createUser(name, password);

        admin.setAdmin(true);
        users.save(admin);

        return admin;
    }

    private User createUser(final String name, final String password) {
        final User user = new User();

        user.setName(name);
        final String salt = generateSalt();
        user.setPassword(BCrypt.hashpw(password + salt, generateSalt()));
        user.setSalt(salt);

        return user;
    }

    private String generateSalt() {
        return BCrypt.gensalt(config.getPasswordStrength());
    }

    @Override
    public User authenticate(final String name, final String password) {
        final User user = users.findByName(name);

        if (null == user) {
            throw new AuthenticationFailed("No such user with name %s!", name);
        }

        if (!BCrypt.checkpw(password + user.getSalt(), user.getPassword())) {
            throw new AuthenticationFailed("Password wrong for user '%s'!", user);
        }

        return user;
    }
}
