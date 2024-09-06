package dev.nathanlively.application;

import dev.nathanlively.application.port.UserRepository;
import dev.nathanlively.security.InMemoryUserRepository;
import dev.nathanlively.security.Role;
import dev.nathanlively.security.User;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static dev.nathanlively.application.ResultAssertions.assertThat;

class UserRegistrationServiceTest {
    @Test
    void with() throws Exception {
        UserRepository repository = InMemoryUserRepository.createEmpty();
        UserRegistrationService service = new UserRegistrationService(repository);
        String username = "travsi@micework.ch";
        String password = "<PASSWORD>";
        User expected = new User(username, "Travis", password, Collections.singleton(Role.USER), new byte[0]);

        Result<User> actual = service.with(username, password);

        assertThat(actual).isSuccess();
        assertThat(actual.failureMessages()).isEmpty();
        assertThat(actual).successValues().contains(expected);

    }
}