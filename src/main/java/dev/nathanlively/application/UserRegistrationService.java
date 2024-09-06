package dev.nathanlively.application;

import dev.nathanlively.application.port.UserRepository;
import dev.nathanlively.security.Role;
import dev.nathanlively.security.User;

import java.util.Collections;
import java.util.List;

public class UserRegistrationService {
    private final UserRepository userRepository;

    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Result<User> with(String username, String password) {
        List<String> validationErrors = InputValidator.validateInputs(username, password);
        if (!validationErrors.isEmpty()) {
            return Result.failure(validationErrors);
        }
        if (userRepository.findByUsername(username) != null) {
            return Result.failure("Username already exists");
        }

        User user = new User(username, username, password, Collections.singleton(Role.USER), new byte[0]);
        return Result.success(user);
    }
}
