package dev.nathanlively.adapter.out.eclipse;

import dev.nathanlively.application.port.UserRepository;
import dev.nathanlively.domain.DataRoot;
import dev.nathanlively.security.User;
import org.eclipse.serializer.persistence.types.Storer;
import org.eclipse.store.integrations.spring.boot.types.concurrent.Read;
import org.eclipse.store.integrations.spring.boot.types.concurrent.Write;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;

public class EclipseUserAdapter implements UserRepository {
    private final EmbeddedStorageManager storageManager;

    public EclipseUserAdapter(EmbeddedStorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Read
    @Override
    public User findByUsername(String username) {
        DataRoot root = (DataRoot) storageManager.root();
        return root.users().byUsername(username);
    }

    @Write
    public void save(User user) {
        DataRoot root = (DataRoot) storageManager.root();
        root.users().add(user);
        saveWithEagerStoring(root);  // Use eager storing to ensure persistence
    }
//
//    @Read
//    public List<User> findAll() {
//        DataRoot root = (DataRoot) storageManager.root();
//        return new ArrayList<>(root.users().all());
//    }
//
//    @Read
//    public List<String> findAllUsernames() {
//        DataRoot root = (DataRoot) storageManager.root();
//        return new ArrayList<>(root.users().getAllUsernames());
//    }
//
    private void saveWithEagerStoring(DataRoot root) {
        Storer eagerStorer = storageManager.createEagerStorer();
        eagerStorer.store(root);
        eagerStorer.commit();
    }
}
