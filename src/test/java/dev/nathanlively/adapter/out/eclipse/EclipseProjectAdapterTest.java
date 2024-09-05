package dev.nathanlively.adapter.out.eclipse;

import dev.nathanlively.domain.DataRoot;
import dev.nathanlively.domain.Project;
import org.eclipse.store.storage.embedded.configuration.types.EmbeddedStorageConfiguration;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageFoundation;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("database")
class EclipseProjectAdapterTest {

    private EmbeddedStorageManager storageManager;
    private EclipseProjectAdapter adapter;
    private Path storageDir;
    private static final Logger log = LoggerFactory.getLogger(EclipseProjectAdapterTest.class);

    @BeforeEach
    void setUp() {
        storageDir = Paths.get(System.getProperty("java.io.tmpdir"), "test-store-" + UUID.randomUUID());
        startStorageManager();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (storageManager != null) {
            storageManager.shutdown();
        }
        if (Files.exists(storageDir)) {
            deleteDirectoryRecursively(storageDir);
        }
    }

    @Test
    void canWriteReadRestartAndReadAgain() {
        writeData();
        storageManager.shutdown();
        log.info("Storage manager shut down. Restarting...");

        try {  // Adding a delay to ensure persistence. Probably not necessary.
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        startStorageManager();
        readData();
    }

    private void startStorageManager() {
        EmbeddedStorageFoundation<?> foundation = EmbeddedStorageConfiguration.Builder()
                .setStorageDirectory(storageDir.toString())
                .createEmbeddedStorageFoundation();
        storageManager = foundation.createEmbeddedStorageManager().start();
        if (storageManager.root() == null) {
            DataRoot root = new DataRoot();
            storageManager.setRoot(root);
        }
        adapter = new EclipseProjectAdapter(storageManager);
    }

    private void writeData() {
        List<Project> projects = adapter.findAll();
        assertThat(projects).isEmpty();

        Project project = Project.create("Project A");
        adapter.save(project);
        storageManager.storeRoot();  // Explicitly save root. Why?

        List<Project> allProjects = adapter.findAll();
        assertThat(allProjects).hasSize(1);
        assertThat(allProjects.getFirst().name()).isEqualTo("Project A");

        List<String> projectNames = adapter.findAllNames();
        assertThat(projectNames).contains("Project A");
    }

    private void readData() {
        List<Project> allProjects = adapter.findAll();
        assertThat(allProjects).hasSize(1);
        assertThat(allProjects.getFirst().name()).isEqualTo("Project A");

        List<String> projectNames = adapter.findAllNames();
        assertThat(projectNames).contains("Project A");
    }

    private void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.exists(path)) {
            try (Stream<Path> walk = Files.walk(path)) {
                walk.sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        });
            }
        } else {
            log.warn("Deletion path does not exist: {}", path);
        }
    }
}