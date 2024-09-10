package dev.nathanlively.domain;

import java.util.*;

public class Resources {
    private final Map<String, Resource> emailToResource = new HashMap<>();

    public Resources() {
        super();
    }

    public void add(final Resource resource) {
        this.addToCollection(resource);
    }

    public void addAll(final Collection<? extends Resource> resources) {
        resources.forEach(this::addToCollection);
    }

    private void addToCollection(final Resource resource) {
        this.emailToResource.put(resource.email(), resource);
    }

    public List<Resource> all() {
        return this.emailToResource.values().stream().sorted(Comparator.comparing(Resource::email)).toList();
    }

    public Resource byEmail(String email) {
        return this.emailToResource.get(email);
    }

    public Set<String> getAllEmails() {
        return new TreeSet<>(emailToResource.keySet());
    }

    public List<TimesheetEntry> timesheetEntriesByProject(Project project) {
        Objects.requireNonNull(project, "Project cannot be null");
        return this.emailToResource.values().stream()
                .flatMap(resource -> resource.timesheet().entriesByProject(project).stream())
                .toList();
    }

    public float totalTimesheetEntryHours(Project project) {
        return 0;
    }
}
