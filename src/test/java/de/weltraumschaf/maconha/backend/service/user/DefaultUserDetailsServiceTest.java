package de.weltraumschaf.maconha.backend.service.user;

import de.weltraumschaf.maconha.backend.model.entity.User;
import de.weltraumschaf.maconha.backend.repo.UserRepo;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link DefaultUserDetailsService}.
 */
public final class DefaultUserDetailsServiceTest {
    private final UserRepo users = mock(UserRepo.class);
    private final DefaultUserDetailsService sut = new DefaultUserDetailsService(users);

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_userNotFound() {
        sut.loadUserByUsername("alice@flower.org");
    }

    @Test
    public void loadUserByUsername() {
        final User alice = new User();
        alice.setName("alice");
        alice.setEmail("alice@flower.org");
        alice.setPassword("password");
        alice.setRole("role");
        when(users.findByName("alice")).thenReturn(alice);

        final UserDetails user = sut.loadUserByUsername("alice");
        final org.springframework.security.core.userdetails.User expected = new org.springframework.security.core.userdetails.User(
            "alice@flower.org",
            "password",
            Collections.singletonList(new SimpleGrantedAuthority("role"))
        );

        assertThat(user.getUsername(), is(expected.getUsername()));
        assertThat(user.getPassword(), is(expected.getPassword()));
        assertThat(user.getAuthorities(), hasSize(1));
        assertThat(user.getAuthorities().iterator().next(), is(new SimpleGrantedAuthority("role")));
    }
}
