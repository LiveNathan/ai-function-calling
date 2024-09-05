package dev.nathanlively.domain;

import java.io.Serializable;

public class DataRoot implements Serializable {
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
