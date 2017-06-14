package de.weltraumschaf.maconha.service.user;

import de.weltraumschaf.maconha.config.MaconhaConfiguration;
import de.weltraumschaf.maconha.model.User;
import de.weltraumschaf.maconha.repo.UserRepo;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link DefaultUserService}.
 */
public final class DefaultUserServiceTest {
    private final UserRepo users = mock(UserRepo.class);
    private final DefaultUserService sut = new DefaultUserService(users, new MaconhaConfiguration());

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
}
