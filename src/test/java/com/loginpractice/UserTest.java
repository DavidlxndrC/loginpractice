package com.loginpractice;

import com.loginpractice.domain.Role;
import com.loginpractice.domain.Status;
import com.loginpractice.domain.User;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    @Test
    void shouldCreateUserCorrectly() {

        User user = new User(
                "juan123",
                "Juan Perez",
                Role.STUDENT,
                Status.ACTIVE,
                "password-hash"
        );

        assertEquals("juan123", user.getUserName());
        assertEquals("Juan Perez", user.getFullName());
        assertEquals(Role.STUDENT, user.getRole());
        assertEquals(Status.ACTIVE, user.getStatus());
        assertEquals("password-hash", user.getPasswordHash());
    }
}