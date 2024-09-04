package dev.nathanlively.domain;

public class DataRoot {
    private final Projects projects = new Projects();

    public DataRoot() {
        super();
    }

    public Projects projects() {
        return projects;
    }
}
