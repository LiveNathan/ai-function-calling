package dev.nathanlively.application.port;

import dev.nathanlively.security.User;

public interface UserRepository {
    User findByUsername(String username);

    void save(User user);
}
