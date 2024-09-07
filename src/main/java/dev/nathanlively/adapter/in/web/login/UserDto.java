package dev.nathanlively.adapter.in.web.login;

public record UserDto(String username, String hashedPassword, String name) {
}
