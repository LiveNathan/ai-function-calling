package dev.nathanlively.application;

import dev.nathanlively.adapter.in.web.login.UserDto;
import dev.nathanlively.adapter.in.web.login.UserMapper;
import dev.nathanlively.application.port.UserRepository;
import dev.nathanlively.security.Role;
import dev.nathanlively.security.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Result<User> register(UserDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()) != null) {
            return Result.failure("Username already exists");
        }
        User user = UserMapper.INSTANCE.fromDto(userDto);
        user.setRoles(Collections.singleton(Role.USER));
        user.setProfilePicture(new byte[0]);
        user.setHashedPassword(passwordEncoder.encode(userDto.getPassword()));

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
        if (!passwordEncoder.matches(userDto.getPassword(), user.getHashedPassword())) {
            return Result.failure("Invalid password");
        }
        return Result.success(user);
    }
}
