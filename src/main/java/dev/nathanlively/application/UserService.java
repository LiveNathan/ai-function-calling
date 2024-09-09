package dev.nathanlively.application;

import dev.nathanlively.adapter.in.web.login.UserDto;
import dev.nathanlively.adapter.in.web.login.UserMapper;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.application.port.UserRepository;
import dev.nathanlively.domain.JobTitle;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.ResourceType;
import dev.nathanlively.security.Role;
import dev.nathanlively.security.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

public class UserService {
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ResourceRepository resourceRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
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

        Resource resource = Resource.create(ResourceType.FULL_TIME, JobTitle.PROGRAMMER, userDto.getName(), userDto.getUsername(), null);

        try {
            userRepository.save(user);
            resourceRepository.save(resource);
        } catch (Exception e) {
            return Result.failure("Problem saving user: " + e.getMessage());
        }
        return Result.success(user);
    }
}
