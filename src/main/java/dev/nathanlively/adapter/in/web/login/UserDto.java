package dev.nathanlively.adapter.in.web.login;

import java.util.Objects;

public  class UserDto {
    private  String username;
    private  String password;
    private  String name;

    public UserDto() {
    }

    public UserDto(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setHashedPassword(String hashedPassword) {
        this.password = hashedPassword;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserDto) obj;
        return Objects.equals(this.username, that.username) &&
                Objects.equals(this.password, that.password) &&
                Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, name);
    }

    @Override
    public String toString() {
        return "UserDto[" +
                "getUsername=" + username + ", " +
                "getPassword=" + password + ", " +
                "getName=" + name + ']';
    }

}
