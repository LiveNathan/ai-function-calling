package dev.nathanlively.adapter.in.web.login;

import dev.nathanlively.security.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    @Test
    void mapToObject() throws Exception {
        UserDto userDto = new UserDto("nathanlively@gmail.com", "password", "Nathan");
        User actual = UserMapper.INSTANCE.userDtoToUser(userDto);
        assertThat(actual).isNotNull();

    }

}