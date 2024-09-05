package dev.nathanlively.domain;

import java.util.*;

public class Projects {
    private final Map<String, Project> nameToProject = new HashMap<>();

    public Projects() {
        super();
    }

    public void add(final Project project) {
        this.addToCollection(project);
    }

    public void addAll(final Collection<? extends Project> projects) {
        projects.forEach(this::addToCollection);
    }

    private void addToCollection(final Project project) {
        this.nameToProject.put(project.name(), project);
    }

    public List<Project> all() {
        return this.nameToProject.values().stream().sorted().toList();
    }

    public Project byName(String projectName) {
        return this.nameToProject.get(projectName);
    }

    public Set<String> getAllNames() {
        return new TreeSet<>(nameToProject.keySet());
    }
}
