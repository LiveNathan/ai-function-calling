package dev.nathanlively.application;

import dev.nathanlively.application.port.UserRepository;
import dev.nathanlively.security.Role;
import dev.nathanlively.security.User;

import java.util.Collections;

public class UserRegistrationService {
    private final UserRepository userRepository;

    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Result<User> with(String username, String password) {
        User user = new User(username, "Travis", password, Collections.singleton(Role.USER), new byte[0]);
        return Result.success(user);
    }
}
