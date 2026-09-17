package com.loginpractice;

import com.loginpractice.access.DatabaseConnection;
import com.loginpractice.access.DatabaseInitializer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseInitializerTest {

    private static final String TEST_URL =
            "jdbc:sqlite:file:testdb?mode=memory&cache=shared";

    private Connection keepAliveConnection;

    @BeforeEach
    void setUp() throws Exception {
        DatabaseConnection.setDatabaseUrl(TEST_URL);
        keepAliveConnection = DriverManager.getConnection(TEST_URL);
    }

    @AfterEach
    void tearDown() throws Exception {
        keepAliveConnection.close();
        DatabaseConnection.resetToDefault();
    }

    @Test
    void shouldCreateUsersTable() throws SQLException {

        DatabaseInitializer.initialize();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT name FROM sqlite_master WHERE type='table' AND name='users'"
             )) {

            assertTrue(resultSet.next());
        }
    }
}