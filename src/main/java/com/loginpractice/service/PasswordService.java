package com.loginpractice.service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordService {

    private final Argon2 argon2;

    public PasswordService() {
        this.argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    }

    public boolean isValidPassword(String password) {

        if (password == null) {
            return false;
        }

        if (password.length() < 6) {
            return false;
        }

        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecialCharacter = password.matches(".*[^a-zA-Z0-9].*");

        return hasUppercase && hasDigit && hasSpecialCharacter;
    }

    public String hashPassword(String password) {

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password does not meet the required format");
        }

        char[] passwordChars = password.toCharArray();

        try {
            return argon2.hash(
                    2,
                    65536,
                    1,
                    passwordChars
            );
        } finally {
            argon2.wipeArray(passwordChars);
        }
    }

    public boolean verifyPassword(String password, String passwordHash) {

        if (password == null || passwordHash == null) {
            return false;
        }

        char[] passwordChars = password.toCharArray();

        try {
            return argon2.verify(
                    passwordHash,
                    passwordChars
            );
        } finally {
            argon2.wipeArray(passwordChars);
        }
    }
}