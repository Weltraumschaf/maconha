package de.weltraumschaf.maconha.app;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link ApplicationConfiguration}.
 */
public final class ApplicationConfigurationTest {
    private final ApplicationConfiguration sut = new ApplicationConfiguration();

    @Test
    public void passwordEncoder() {
        assertThat(sut.passwordEncoder(), is(not(nullValue())));
        assertThat(sut.passwordEncoder() instanceof BCryptPasswordEncoder, is(true));
    }

    @Test
    public void securedViewAccessControl() {
        assertThat(sut.securedViewAccessControl(), is(not(nullValue())));
    }
}
