package dev.nathanlively.domain;

import dev.nathanlively.security.Users;

public class DataRoot {
    private final Projects projects = new Projects();
    private final Resources resources = new Resources();
    private final Users users = new Users();

    public DataRoot() {
        super();
    }

    public Projects projects() {
        return projects;
    }

    public Resources resources() {
        return resources;
    }

    public Users users() {
        return users;
    }
}
