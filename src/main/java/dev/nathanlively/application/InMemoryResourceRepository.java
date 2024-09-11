package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryResourceRepository implements ResourceRepository {

    private final Map<String, Resource> resources;

    public InMemoryResourceRepository(Map<String, Resource> resources) {
        this.resources = resources;
    }

    public static InMemoryResourceRepository create(Map<String, Resource> resources) {
        return new InMemoryResourceRepository(resources);
    }

    public static ResourceRepository createEmpty() {
        return create(new HashMap<>());
    }

    @Override
    public void save(Resource resource) {
        resources.put(resource.email(), resource);
    }

    @Override
    public List<Resource> findAll() {
        return List.copyOf(resources.values());
    }

    @Override
    public Optional<Resource> findByEmail(String resourceEmail) {
        return Optional.ofNullable(resources.get(resourceEmail));
    }

    @Override
    public List<TimesheetEntry> timesheetEntriesByProject(Project project) {
        return findAll().stream()
                .flatMap(resource -> resource.timesheet().entriesByProject(project).stream())
                .toList();
    }

    @Override
    public float totalTimesheetEntryHours(Project project) {
        long totalMinutes = timesheetEntriesByProject(project).stream()
                .mapToLong(entry -> entry.duration().toMinutes())
                .sum();
        return Math.round(totalMinutes / 60.0f * 100.0f) / 100.0f;  // precision 0.01
    }
}
