package dev.nathanlively.adapter.out.eclipse;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.DataRoot;
import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.TimesheetEntry;
import org.eclipse.serializer.persistence.types.Storer;
import org.eclipse.store.integrations.spring.boot.types.concurrent.Read;
import org.eclipse.store.integrations.spring.boot.types.concurrent.Write;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EclipseResourceAdapter implements ResourceRepository {
    private final EmbeddedStorageManager storageManager;

    public EclipseResourceAdapter(EmbeddedStorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Write
    @Override
    public void save(Resource resource) {
        DataRoot root = (DataRoot) storageManager.root();
        root.resources().add(resource);
        saveWithEagerStoring(root);
    }

    @Read
    @Override
    public List<Resource> findAll() {
        DataRoot root = (DataRoot) storageManager.root();
        return new ArrayList<>(root.resources().all());
    }

    @Read
    @Override
    public Optional<Resource> findByEmail(String resourceEmail) {
        DataRoot root = (DataRoot) storageManager.root();
        return Optional.ofNullable(root.resources().byEmail(resourceEmail));
    }

    @Override
    public List<TimesheetEntry> timesheetEntriesByProject(Project project) {
        DataRoot root = (DataRoot) storageManager.root();
        return root.resources().timesheetEntriesByProject(project);
    }

    private void saveWithEagerStoring(DataRoot root) {
        Storer eagerStorer = storageManager.createEagerStorer();
        eagerStorer.store(root);
        eagerStorer.commit();
    }
}
