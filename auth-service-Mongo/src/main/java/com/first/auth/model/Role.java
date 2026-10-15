package com.first.auth.model;

import java.util.Collection;

public enum Role {
    USER,
    ADMIN;

    /**
     * The single effective role reported to clients: ADMIN if the user holds it, otherwise USER.
     */
    public static Role primaryOf(Collection<Role> roles) {
        return roles != null && roles.contains(ADMIN) ? ADMIN : USER;
    }
}
