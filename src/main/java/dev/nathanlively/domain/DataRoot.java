package dev.nathanlively.domain;

public class DataRoot {
    private final Projects projects = new Projects();
    private final Resources resources = new Resources();

    public DataRoot() {
        super();
    }

    public Projects projects() {
        return projects;
    }

    public Resources resources() {
        return resources;
    }
}
