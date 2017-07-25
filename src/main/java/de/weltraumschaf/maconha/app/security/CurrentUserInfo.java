package de.weltraumschaf.maconha.app.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CurrentUserInfo takes care of all such static operations that have to do with security and querying rights from
 * different beans of the UI.
 */
public final class CurrentUserInfo {

    private final SecurityContext context;

    public CurrentUserInfo() {
        this(SecurityContextHolder.getContext());
    }

    CurrentUserInfo(final SecurityContext context) {
        super();
        this.context = Objects.requireNonNull(context, "Parameter 'context' must not be null!");
    }

    /**
     * Gets the user name of the currently signed in user.
     *
     * @return the user name of the current user or {@code null} if the
     * user has not signed in
     */
    public String usersName() {
        final UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * Check if currently signed-in user is in the role with the given role name.
     *
     * @param role must not be {@code null}
     * @return {@code true} if user is in the role, {@code false} otherwise
     */
    public boolean isInRole(final String role) {
        Objects.requireNonNull(role, "Parameter 'role' must not be null!");
        return roles()
            .stream()
            .anyMatch(roleName -> roleName.equals(role));
    }

    /**
     * Gets the roles the currently signed-in user belongs to.
     *
     * @return never {@code null}, unmodifiable
     */
    Set<String> roles() {
        final Set<String> roles = context.getAuthentication()
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
        return Collections.unmodifiableSet(roles);
    }

}
