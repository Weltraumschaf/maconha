package de.weltraumschaf.maconha.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.weltraumschaf.maconha.model.User;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
@Service("userService")
final class UserServiceImpl implements UserService {

    private static final AtomicInteger counter = new AtomicInteger();

    private static final List<User> users;

    static {
        users = populateDummyUsers();
    }

    @Override
    public List<User> findAllUsers() {
        return users;
    }

    @Override
    public User findById(long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findByName(String name) {
        for (final User user : users) {
            if (user.getUsername().equalsIgnoreCase(name)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public void saveUser(User user) {
        user.setId(counter.incrementAndGet());
        users.add(user);
    }

    @Override
    public void updateUser(User user) {
        int index = users.indexOf(user);
        users.set(index, user);
    }

    @Override
    public void deleteUserById(long id) {

        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User user = iterator.next();
            if (user.getId() == id) {
                iterator.remove();
            }
        }
    }

    @Override
    public boolean isUserExist(User user) {
        return findByName(user.getUsername()) != null;
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }

    private static List<User> populateDummyUsers() {
        List<User> users = new ArrayList<>();
        return users;
    }

}
