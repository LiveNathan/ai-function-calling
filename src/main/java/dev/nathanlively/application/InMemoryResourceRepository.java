package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Resource;

import java.util.*;

public class InMemoryResourceRepository implements ResourceRepository {
    private final Map<String, Resource> resources;

    public InMemoryResourceRepository(Map<String, Resource> resources) {
        this.resources = resources;
    }

    public static InMemoryResourceRepository create(List<Resource> resourcesList) {
        Map<String, Resource> resourceMap = new HashMap<>();
        for (Resource resource : resourcesList) {
            resourceMap.put(resource.email(), resource);
        }
        return new InMemoryResourceRepository(resourceMap);
    }

    public static ResourceRepository createEmpty() {
        return new InMemoryResourceRepository(new HashMap<>());
    }

    @Override
    public void save(Resource resource) {
        resources.put(resource.email(), resource);
    }

    @Override
    public List<Resource> findAll() {
        return new ArrayList<>(resources.values());
    }

    @Override
    public Optional<Resource> findByEmail(String resourceEmail) {
        return Optional.ofNullable(resources.get(resourceEmail));
    }
}
