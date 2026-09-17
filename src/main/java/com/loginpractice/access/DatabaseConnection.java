package com.loginpractice.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static String databaseUrl = "jdbc:sqlite:database.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }

    public static void setDatabaseUrl(String url) {
        databaseUrl = url;
    }

    public static void resetToDefault() {
        databaseUrl = "jdbc:sqlite:database.db";
    }
}