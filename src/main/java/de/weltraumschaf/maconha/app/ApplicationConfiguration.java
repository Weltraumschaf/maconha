package de.weltraumschaf.maconha.app;

import com.vaadin.spring.access.SecuredViewAccessControl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * General application configuration.
 */
@Configuration
public class ApplicationConfiguration {

    /**
     * The password encoder to use when encrypting passwords.
     *
     * @return never {@code null}, always new instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Special implementation to use Spring Security in conjunction with Vaadin.
     *
     * @return never {@code null}, always new instance
     */
    @Bean
    public SecuredViewAccessControl securedViewAccessControl() {
        return new SecuredViewAccessControl();
    }

}
