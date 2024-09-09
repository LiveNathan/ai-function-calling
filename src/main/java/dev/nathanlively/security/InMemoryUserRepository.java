package dev.nathanlively.security;

import dev.nathanlively.application.port.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> usernameToUser;

    public InMemoryUserRepository(Map<String, User> usernameToUser) {
        this.usernameToUser = usernameToUser;
    }

    public static InMemoryUserRepository create(Map<String, User> users) {
        return new InMemoryUserRepository(users);
    }

    public static UserRepository createEmpty() {
        return create(new HashMap<>());
    }

    @Override
    public User findByUsername(String username) {
        return usernameToUser.get(username);
    }

    @Override
    public void save(User user) {
        usernameToUser.put(user.getUsername(), user);
    }

    public List<User> findAll() {
        return new ArrayList<>(usernameToUser.values());
    }

    public List<String> findAllUsernames() {
        return usernameToUser.keySet().stream().toList();
    }
}
