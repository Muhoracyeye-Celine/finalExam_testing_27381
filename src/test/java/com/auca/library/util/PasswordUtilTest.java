package com.auca.library.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class PasswordUtilTest {

    @Test
    public void hashPassword_returnsEncryptedValue() {
        String rawPassword = "Library@123";

        String passwordHash =
                PasswordUtil.hashPassword(rawPassword);

        assertNotEquals(rawPassword, passwordHash);
    }

    @Test
    public void correctPassword_matchesStoredHash() {
        String rawPassword = "Library@123";

        String passwordHash =
                PasswordUtil.hashPassword(rawPassword);

        assertTrue(
                PasswordUtil.matches(
                        rawPassword,
                        passwordHash
                )
        );
    }

    @Test
    public void wrongPassword_doesNotMatchStoredHash() {
        String passwordHash =
                PasswordUtil.hashPassword("Library@123");

        assertFalse(
                PasswordUtil.matches(
                        "WrongPassword",
                        passwordHash
                )
        );
    }
}