package de.weltraumschaf.maconha.app.security;

import de.weltraumschaf.maconha.app.Application;
import de.weltraumschaf.maconha.backend.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class configures the authentication and access control to the web frontend URLs.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RedirectToBackendUi successHandler;

    @Autowired
    public SecurityConfig(
        final UserDetailsService userDetailsService,
        final PasswordEncoder passwordEncoder,
        final RedirectToBackendUi successHandler) {
        super();
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.successHandler = successHandler;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // Not using Spring CSRF here to be able to use plain HTML for the login page.
        http.csrf().disable();

        http
            .authorizeRequests()
            // From strong to weak access level.
            // https://stackoverflow.com/questions/36382970/allow-all-urls-but-one-in-spring-security
            .antMatchers(Application.ADMIN_URL).hasAnyAuthority(Role.getAllRoles())
            .antMatchers(Application.ADMIN_URL + "/**").hasAnyAuthority(Role.getAllRoles())
            .antMatchers("/**").permitAll()
            .and()
            .formLogin().permitAll()
            .loginPage(Application.LOGIN_URL).loginProcessingUrl(Application.LOGIN_PROCESSING_URL)
            .failureUrl(Application.LOGIN_FAILURE_URL).successHandler(successHandler)
            .and()
            .logout().logoutSuccessUrl(Application.LOGOUT_URL);
    }

}
