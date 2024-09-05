package dev.nathanlively.adapter.out.eclipse;

import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.DataRoot;
import dev.nathanlively.domain.Resource;
import org.eclipse.serializer.persistence.types.Storer;
import org.eclipse.store.integrations.spring.boot.types.concurrent.Read;
import org.eclipse.store.integrations.spring.boot.types.concurrent.Write;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EclipseResourceAdapter implements ResourceRepository {

    private final EmbeddedStorageManager storageManager;
    private static final Logger log = LoggerFactory.getLogger(EclipseResourceAdapter.class);

    public EclipseResourceAdapter(EmbeddedStorageManager storageManager) {
        this.storageManager = storageManager;
//        ensureRootInitialized();
    }

    private void ensureRootInitialized() {
        if (storageManager.root() == null) {
            DataRoot root = new DataRoot();
            storageManager.setRoot(root);
            saveWithEagerStoring(root);
        } else {
            log.info("Root already initialized");
        }
    }

    @Write
    @Override
    public void save(Resource resource) {
        DataRoot root = (DataRoot) storageManager.root();
        root.resources().add(resource);
//        storageManager.store(root.projects());
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

    private void saveWithEagerStoring(DataRoot root) {
        Storer eagerStorer = storageManager.createEagerStorer();
        eagerStorer.store(root);
        eagerStorer.commit();
    }
}
