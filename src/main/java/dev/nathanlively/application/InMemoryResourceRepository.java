package dev.nathanlively.application;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryResourceRepository implements ResourceRepository {
    private final List<Resource> resources;

    public InMemoryResourceRepository(List<Resource> resources) {
        this.resources = resources;
    }

    public static InMemoryResourceRepository create(List<Resource> resources) {
        return new InMemoryResourceRepository(resources);
    }

    public static ResourceRepository createEmpty() {
        return create(new ArrayList<>());
    }

    @Override
    public void save(Resource resource) {
        resources.add(resource);
    }

    @Override
    public List<Resource> findAll() {
        return resources;
    }

    @Override
    public Optional<Resource> findByEmail(String resourceEmail) {
        for (Resource resource : resources) {
            if (resource.email().equals(resourceEmail)) {
                return Optional.of(resource);
            }
        }
        return Optional.empty();
    }
}
