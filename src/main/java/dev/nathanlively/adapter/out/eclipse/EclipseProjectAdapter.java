package dev.nathanlively.adapter.out.eclipse;

import dev.nathanlively.application.ProjectNameMatcher;
import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.domain.DataRoot;
import dev.nathanlively.domain.Project;
import org.eclipse.store.integrations.spring.boot.types.concurrent.Read;
import org.eclipse.store.integrations.spring.boot.types.concurrent.Write;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EclipseProjectAdapter implements ProjectRepository {

    private final EmbeddedStorageManager storageManager;

    public EclipseProjectAdapter(EmbeddedStorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Write
    @Override
    public void save(Project project) {
        DataRoot root = (DataRoot) storageManager.root();
        root.projects().add(project);
        storageManager.store(root.projects());
    }

    @Read
    @Override
    public List<Project> findAll() {
        DataRoot root = (DataRoot) storageManager.root();
        return new ArrayList<>(root.projects().all()); // Create new List... never return original one.
    }

    @Read
    @Override
    public Optional<Project> findByName(String projectName) {
        List<String> allNames = findAllNames();
        String cleanedName = ProjectNameMatcher.from(projectName, allNames).orElse(null);
        if (cleanedName == null) return Optional.empty();
        DataRoot root = (DataRoot) storageManager.root();
        return Optional.ofNullable(root.projects().byName(projectName));
    }

    @Read
    @Override
    public List<String> findAllNames() {
        DataRoot root = (DataRoot) storageManager.root();
        return new ArrayList<>(root.projects().getAllNames());
    }
}
