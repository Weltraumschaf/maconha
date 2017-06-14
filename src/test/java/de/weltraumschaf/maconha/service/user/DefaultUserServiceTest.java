package de.weltraumschaf.maconha.service.user;

import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.core.Crypt;
import de.weltraumschaf.maconha.model.User;
import de.weltraumschaf.maconha.repo.UserRepo;
import de.weltraumschaf.maconha.service.UserService;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link DefaultUserService}.
 */
public final class DefaultUserServiceTest {
    private final UserRepo users = mock(UserRepo.class);
    private final MaconhaConfiguration config = new MaconhaConfiguration();
    private final Crypt crypt = mock(Crypt.class);
    private final DefaultUserService sut = new DefaultUserService(users, config, crypt);

    @Test
    public void isThereNoAdminUser_trueIfNoUserPresent() {
        when(users.findByAdmin(true)).thenReturn(Collections.emptyList());

        assertThat(sut.isThereNoAdminUser(), is(true));
    }

    @Test
    public void isThereNoAdminUser_falseIfUserPresent() {
        when(users.findByAdmin(true)).thenReturn(Collections.singletonList(new User()));

        assertThat(sut.isThereNoAdminUser(), is(false));
    }

    @Test
    public void createUnprivileged() {
        when(crypt.gensalt(config.getPasswordStrength())).thenReturn("salt");
        when(crypt.hashpw("pass", "salt")).thenReturn("****");

        final User user = sut.createUnprivileged("user", "pass");

        assertThat(user.getName(), is("user"));
        assertThat(user.getPassword(), is("****"));
        assertThat(user.isAdmin(), is(false));
        verify(users, times(1)).save(user);
    }

    @Test
    public void createAdmin() {
        when(crypt.gensalt(config.getPasswordStrength())).thenReturn("salt");
        when(crypt.hashpw("pass", "salt")).thenReturn("****");

        final User user = sut.createAdmin("user", "pass");

        assertThat(user.getName(), is("user"));
        assertThat(user.getPassword(), is("****"));
        assertThat(user.isAdmin(), is(true));
        verify(users, times(1)).save(user);
    }

    @Test(expected = UserService.AuthenticationFailed.class)
    public void authenticate_throwsExceptionIfUserIsNull() {
        sut.authenticate("user", "pass");
    }

    @Test(expected = UserService.AuthenticationFailed.class)
    public void authenticate_throwsExceptionIfPasswordDoesNotMatch() {
        when(users.findByName("user")).thenReturn(new User());
        when(crypt.checkpw(anyString(), anyString())).thenReturn(false);

        sut.authenticate("user", "pass");
    }

    @Test
    public void authenticate_returnsUserIfAuthenticated() {
        final User user = new User();
        user.setPassword("hash");
        when(users.findByName("user")).thenReturn(user);
        when(crypt.checkpw("pass", "hash")).thenReturn(true);

        assertThat(sut.authenticate("user", "pass"), is(user));
    }
}
