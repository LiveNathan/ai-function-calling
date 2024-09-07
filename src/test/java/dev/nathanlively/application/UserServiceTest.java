package dev.nathanlively.application;

import dev.nathanlively.adapter.in.web.login.UserDto;
import dev.nathanlively.adapter.in.web.login.UserMapper;
import dev.nathanlively.application.port.UserRepository;
import dev.nathanlively.security.InMemoryUserRepository;
import dev.nathanlively.security.Role;
import dev.nathanlively.security.User;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class UserServiceTest {
    @Test
    void register() {
        UserRepository repository = InMemoryUserRepository.createEmpty();
        assertThat(repository.findAll()).hasSize(0);
        UserService service = new UserService(repository);
        String username = "travsi@micework.ch";
        String password = "<PASSWORD>";
        User expected = new User(username, "Travis", password, Collections.singleton(Role.USER), new byte[0]);
        UserDto userDto = UserMapper.INSTANCE.fromUser(expected);
        assertThat(userDto.getName()).isNotEmpty();

        Result<User> actual = service.register(userDto);

        assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        assertThat(actual).successValues().contains(expected);

        assertThat(repository.findAll()).hasSize(1);
    }
}