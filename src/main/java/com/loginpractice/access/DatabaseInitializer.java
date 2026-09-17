package com.loginpractice.access;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    full_name TEXT NOT NULL,
                    role TEXT NOT NULL CHECK (
                        role IN (
                            'ADMIN',
                            'QUESTION_AUTHOR',
                            'REVIEWER',
                            'TEACHER',
                            'STUDENT'
                        )
                    ),
                    status TEXT NOT NULL CHECK (
                        status IN ('ACTIVE', 'INACTIVE')
                    ),
                    password_hash TEXT NOT NULL
                );
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);
        }
    }
}