package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryResourceRepository implements ResourceRepository {

    private final Map<String, Resource> resources;

    public InMemoryResourceRepository(Map<String, Resource> resources) {
        this.resources = resources;
    }

    public static InMemoryResourceRepository create(List<Resource> resourcesList) {
        Map<String, Resource> resourcesMap = new HashMap<>();
        for (Resource resource : resourcesList) {
            resourcesMap.put(resource.email(), resource);
        }
        return new InMemoryResourceRepository(resourcesMap);
    }

    public static ResourceRepository createEmpty() {
        return create(List.of());
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
}
