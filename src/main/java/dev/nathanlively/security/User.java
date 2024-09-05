package dev.nathanlively.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.server.StreamResource;
import dev.nathanlively.domain.Named;

import java.io.ByteArrayInputStream;
import java.util.Set;

public class User extends Named {
    private String username;
    @JsonIgnore
    private String hashedPassword;
    private Set<Role> roles;
    private byte[] profilePicture;

    public User(String username, String name, String hashedPassword, Set<Role> roles, byte[] profilePicture) {
        super(name);
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.roles = roles;
        this.profilePicture = profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePictureUri() {
        Avatar avatar = new Avatar(name());
        StreamResource resource = new StreamResource("profile-pic", () -> new ByteArrayInputStream(profilePicture));
        avatar.setImageResource(resource);
        return avatar.getImage();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", name='" + name() + '\'' +
                ", roles=" + roles +
                '}';
    }
}
