package de.weltraumschaf.maconha.backend.service.user;

import de.weltraumschaf.maconha.backend.model.entity.User;
import de.weltraumschaf.maconha.backend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Implementation of {@link UserDetailsService} to provide user details.
 */
@Service
public final class DefaultUserDetailsService implements UserDetailsService {

    private final UserRepo userRepository;

    @Autowired
    public DefaultUserDetailsService(final UserRepo userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userRepository.findByName(username);

        if (null == user) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
        }
    }
}