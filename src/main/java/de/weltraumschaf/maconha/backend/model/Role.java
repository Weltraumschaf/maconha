package de.weltraumschaf.maconha.backend.model;

public final class Role {
    public static final String USER = "user";
    public static final String ADMIN = "admin";

    private Role() {
        super();
    }

    public static String[] getAllRoles() {
        return new String[] {USER, ADMIN};
    }

}
