package dev.nathanlively.application.port;

import dev.nathanlively.security.User;

import java.util.List;

public interface UserRepository {
    User findByUsername(String username);

    void save(User user);

    List<User> findAll();
}
