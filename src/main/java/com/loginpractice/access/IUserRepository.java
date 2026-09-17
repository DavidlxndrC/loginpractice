package com.loginpractice.access;

import com.loginpractice.domain.User;

import java.util.Optional;

public interface IUserRepository {

    void save(User user);

    Optional<User> findByUsername(String userName);

    void update(User user);

    void delete(int id);
}