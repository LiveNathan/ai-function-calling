package dev.nathanlively.security;

import java.util.*;

public class Users {

    private final Map<String, User> usernameToUser = new HashMap<>();

    public Users() {
        super();
    }

    public void add(final User user) {
        this.addToCollection(user);
    }

    public void addAll(final Collection<? extends User> users) {
        users.forEach(this::addToCollection);
    }

    private void addToCollection(final User user) {
        this.usernameToUser.put(user.getUsername(), user);
    }

    public List<User> all() {
        return this.usernameToUser.values()
                .stream()
                .sorted(Comparator.comparing(User::getUsername))
                .toList();
    }

    public User byUsername(String username) {
        return this.usernameToUser.get(username);
    }

    public Set<String> getAllUsernames() {
        return new TreeSet<>(usernameToUser.keySet());
    }
}
