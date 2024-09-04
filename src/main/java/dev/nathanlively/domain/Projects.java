package dev.nathanlively.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//        this.addToMap(this.nameToProject, project.name(), project);
    }

//    private <K> void addToMap(final Map<K, List<Project>> map, final K key, final Project project) {
//        map.computeIfAbsent(key, k -> new ArrayList<>(1024)).add(project);
//    }

    public List<Project> all() {
        return this.nameToProject.values().stream().sorted().toList();
    }
}
