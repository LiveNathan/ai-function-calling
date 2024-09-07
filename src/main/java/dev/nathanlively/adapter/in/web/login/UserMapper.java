package dev.nathanlively.adapter.in.web.login;

import dev.nathanlively.security.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    UserDto fromUser(User user);

    @Mapping(source = "password", target = "hashedPassword")
    User fromDto(UserDto userDto);
}
