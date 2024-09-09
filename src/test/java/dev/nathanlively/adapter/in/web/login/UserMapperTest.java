package dev.nathanlively.adapter.in.web.login;

import dev.nathanlively.security.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    @Test
    void mapToObject() {
        String email = "nathanlively@gmail.com";
        String password = "password";
        String nathan = "Nathan";
        UserDto userDto = new UserDto(email, password, nathan);
        User expected = new User(email, nathan, password, null, null);

        User actual = UserMapper.INSTANCE.fromDto(userDto);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapToDto() throws Exception {
        String email = "nathanlively@gmail.com";
        String password = "password";
        String nathan = "Nathan";
        User user = new User(email, nathan, password, null, null);
        assertThat(user.getName()).isNotNull();
        UserDto expected = new UserDto(email, password, nathan);

        UserDto actual = UserMapper.INSTANCE.fromUser(user);

        assertThat(actual).isNotNull();
        assertThat(actual)
                .isEqualTo(expected);
    }

}