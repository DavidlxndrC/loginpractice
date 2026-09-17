package com.loginpractice.access;

import com.loginpractice.domain.Role;
import com.loginpractice.domain.Status;
import com.loginpractice.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserRepository implements IUserRepository {

    @Override
    public void save(User user) {
        String sql = """
                INSERT INTO users
                (username, full_name, role, status, password_hash)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getUserName());
            statement.setString(2, user.getFullName());
            statement.setString(3, user.getRole().name());
            statement.setString(4, user.getStatus().name());
            statement.setString(5, user.getPasswordHash());

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new RuntimeException("Error saving user", exception);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {

        String sql = """
                SELECT id, username, full_name, role, status, password_hash
                FROM users
                WHERE username = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (var resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    User user = new User(
                            resultSet.getString("username"),
                            resultSet.getString("full_name"),
                            Role.valueOf(resultSet.getString("role")),
                            Status.valueOf(resultSet.getString("status")),
                            resultSet.getString("password_hash")
                    );

                    return Optional.of(user);
                }

                return Optional.empty();
            }

        } catch (SQLException exception) {
            throw new RuntimeException(
                    "Error finding user by username",
                    exception
            );
        }
    }

    @Override
    public void update(User user) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}