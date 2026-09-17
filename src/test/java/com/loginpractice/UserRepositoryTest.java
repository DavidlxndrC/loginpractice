package com.loginpractice;

import com.loginpractice.access.DatabaseConnection;
import com.loginpractice.access.DatabaseInitializer;
import com.loginpractice.access.UserRepository;
import com.loginpractice.domain.Role;
import com.loginpractice.domain.Status;
import com.loginpractice.domain.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryTest {

    private static final String TEST_URL =
            "jdbc:sqlite:file:testdb?mode=memory&cache=shared";

    private Connection keepAliveConnection;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {

        DatabaseConnection.setDatabaseUrl(TEST_URL);
        keepAliveConnection = DriverManager.getConnection(TEST_URL);

        DatabaseInitializer.initialize();

        userRepository = new UserRepository();

        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement()) {

            statement.executeUpdate("DELETE FROM users");
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        keepAliveConnection.close();
        DatabaseConnection.resetToDefault();
    }

    @Test
    void shouldSaveUser() {

        User user = new User(
                "firstuser",
                "Test User",
                Role.STUDENT,
                Status.ACTIVE,
                "test-password-hash"
        );

        assertDoesNotThrow(() -> userRepository.save(user));
    }

    @Test
    void shouldFindSavedUser() {

        User user = new User(
                "checkuser",
                "Check User",
                Role.STUDENT,
                Status.ACTIVE,
                "test-password-hash"
        );

        userRepository.save(user);

        var found = userRepository.findByUsername("checkuser");

        assertTrue(found.isPresent());
        assertEquals("Check User", found.get().getFullName());
    }
}