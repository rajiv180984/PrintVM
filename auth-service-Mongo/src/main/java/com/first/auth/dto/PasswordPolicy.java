package com.first.auth.dto;

/**
 * Shared password rule used by every request that sets a password.
 */
public final class PasswordPolicy {

    /** 8-64 chars, at least one upper case, one lower case, one digit and one special character. */
    public static final String REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9])\\S{8,64}$";

    public static final String MESSAGE =
            "Password must be 8-64 characters with no spaces and contain upper case, lower case, digit and special character";

    private PasswordPolicy() {
    }
}
