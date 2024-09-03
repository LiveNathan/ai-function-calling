package dev.nathanlively.domain;

import java.util.Objects;

public class Project {
    private String name;
    private int estimatedHours;

    public Project(String name, int estimatedHours) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be null or blank.");
        }
        this.name = name;
        this.estimatedHours = estimatedHours;
    }

    public static Project create(String name) {
        return new Project(name, 0);
    }

    public void updateEstimatedHours(int estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public String name() {
        return name;
    }

    public int estimatedHours() {
        return estimatedHours;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Project) obj;
        return Objects.equals(this.name, that.name) &&
                this.estimatedHours == that.estimatedHours;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, estimatedHours);
    }

    @Override
    public String toString() {
        return "Project[" +
                "name=" + name + ", " +
                "estimatedHours=" + estimatedHours + ']';
    }

}
