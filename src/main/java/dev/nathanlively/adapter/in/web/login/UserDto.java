package dev.nathanlively.adapter.in.web.login;

import java.util.Objects;

public  class UserDto {
    private  String username;
    private  String hashedPassword;
    private  String name;

    public UserDto() {
    }

    public UserDto(String username, String hashedPassword, String name) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String username() {
        return username;
    }

    public String hashedPassword() {
        return hashedPassword;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserDto) obj;
        return Objects.equals(this.username, that.username) &&
                Objects.equals(this.hashedPassword, that.hashedPassword) &&
                Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, hashedPassword, name);
    }

    @Override
    public String toString() {
        return "UserDto[" +
                "username=" + username + ", " +
                "hashedPassword=" + hashedPassword + ", " +
                "name=" + name + ']';
    }

}
