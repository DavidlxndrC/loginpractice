package co.edu.unicauca.saberpro.access;

import co.edu.unicauca.saberpro.domain.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeUserRepository implements IUserRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public void save(User user) {

        if (users.containsKey(user.getUserName())) {
            throw new RuntimeException("Username already exists");
        }

        users.put(user.getUserName(), user);
    }

    @Override
    public Optional<User> findByUsername(String userName) {
        return Optional.ofNullable(users.get(userName));
    }

    @Override
    public void update(User user) {
        users.put(user.getUserName(), user);
    }

    @Override
    public void delete(int id) {
        users.values().removeIf(user -> user.getId() == id);
    }

    public int size() {
        return users.size();
    }
}