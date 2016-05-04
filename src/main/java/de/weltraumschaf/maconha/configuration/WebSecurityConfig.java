package de.weltraumschaf.maconha.configuration;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configAuthentication(final AuthenticationManagerBuilder auth) throws Exception {
        LOGGER.debug("Configure authentication.");
        auth.jdbcAuthentication().dataSource(dataSource)
            .usersByUsernameQuery(
                "select username,password, enabled from users where username=?")
            .authoritiesByUsernameQuery(
                "select username, role from user_roles where username=?");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        LOGGER.debug("Configure HTTP security.");
        http.authorizeRequests()
            .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
            .anyRequest().permitAll()
            .and()
            .formLogin().loginPage("/login")
            .usernameParameter("username").passwordParameter("password")
            .and()
            .logout().logoutSuccessUrl("/login?logout")
            .and()
            .exceptionHandling().accessDeniedPage("/403")
            .and()
            .csrf();
    }
}
