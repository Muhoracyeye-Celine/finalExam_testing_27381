package com.auca.library.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class PasswordUtil {

    private static final int BCRYPT_COST = 12;

    private PasswordUtil() {
        // Prevents objects of this utility class from being created.
    }

    public static String hashPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters."
            );
        }

        return BCrypt.withDefaults()
                .hashToString(
                        BCRYPT_COST,
                        rawPassword.toCharArray()
                );
    }

    public static boolean matches(
            String rawPassword,
            String passwordHash
    ) {
        if (rawPassword == null
                || rawPassword.isBlank()
                || passwordHash == null
                || passwordHash.isBlank()) {
            return false;
        }

        try {
            return BCrypt.verifyer()
                    .verify(
                            rawPassword.toCharArray(),
                            passwordHash
                    )
                    .verified;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}