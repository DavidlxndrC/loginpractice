package co.edu.unicauca.saberpro;

import co.edu.unicauca.saberpro.access.DatabaseConnection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseConnectionTest {

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
    void shouldConnectToDatabase() throws Exception {

        Connection connection = DatabaseConnection.getConnection();

        assertNotNull(connection);

        connection.close();
    }
}