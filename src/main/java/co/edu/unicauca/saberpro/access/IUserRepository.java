package co.edu.unicauca.saberpro.access;

import co.edu.unicauca.saberpro.domain.model.User;

import java.util.Optional;

public interface IUserRepository {

    void save(User user);

    Optional<User> findByUsername(String userName);

    void update(User user);

    void delete(int id);
}