package dev.nathanlively.application;

import dev.nathanlively.adapter.in.web.login.UserDto;
import dev.nathanlively.adapter.in.web.login.UserMapper;
import dev.nathanlively.application.port.UserRepository;
import dev.nathanlively.security.Role;
import dev.nathanlively.security.User;

import java.util.Collections;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Result<User> register(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            return Result.failure("Username already exists");
        }

        User user = UserMapper.INSTANCE.fromDto(userDto);
        user.setRoles(Collections.singleton(Role.USER));
        user.setProfilePicture(new byte[0]);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return Result.failure("Problem saving user: " + e.getMessage());
        }
        return Result.success(user);
    }

    public Result<User> login(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user == null) {
            return Result.failure("User not found");
        }

        if (!matchesPassword(userDto.getHashedPassword(), user.getHashedPassword())) {
            return Result.failure("Invalid password");
        }

        return Result.success(user);
    }

    // Dummy implementation for password matching. Replace with actual functionality.
    private boolean matchesPassword(String rawPassword, String hashedPassword) {
        // Compare the passwords (e.g., BCrypt, PBKDF2, etc.)
        return rawPassword.equals(hashedPassword);
    }
}
