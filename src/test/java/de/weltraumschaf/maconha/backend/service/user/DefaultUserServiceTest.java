package de.weltraumschaf.maconha.backend.service.user;

import de.weltraumschaf.maconha.backend.model.Role;
import de.weltraumschaf.maconha.backend.model.entity.User;
import de.weltraumschaf.maconha.backend.repo.UserRepo;
import de.weltraumschaf.maconha.backend.service.UserService;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link DefaultUserService}.
 */
public final class DefaultUserServiceTest {
    private final UserRepo users = mock(UserRepo.class);
    private final PasswordEncoder crypt = mock(PasswordEncoder.class);
    private final DefaultUserService sut = new DefaultUserService(users, crypt);

    @Test
    public void isThereNoAdminUser_trueIfNoUserPresent() {
        when(users.findByRole(Role.ADMIN)).thenReturn(Collections.emptyList());

        assertThat(sut.isThereNoAdminUser(), is(true));
    }

    @Test
    public void isThereNoAdminUser_falseIfUserPresent() {
        when(users.findByRole(Role.ADMIN)).thenReturn(Collections.singletonList(new User()));

        assertThat(sut.isThereNoAdminUser(), is(false));
    }

    @Test
    public void createUnprivileged() {
        when(crypt.encode("pass")).thenReturn("****");

        final User user = sut.createUnprivileged("user", "pass", "email");

        assertThat(user.getName(), is("user"));
        assertThat(user.getPassword(), is("****"));
        assertThat(user.getEmail(), is("email"));
        assertThat(user.getRole(), is(Role.USER));
        verify(users, times(1)).save(user);
    }

    @Test
    public void createAdmin() {
        when(crypt.encode("pass")).thenReturn("****");

        final User user = sut.createAdmin("user", "pass", "email");

        assertThat(user.getName(), is("user"));
        assertThat(user.getPassword(), is("****"));
        assertThat(user.getEmail(), is("email"));
        assertThat(user.getRole(), is(Role.ADMIN));
        verify(users, times(1)).save(user);
    }

}
