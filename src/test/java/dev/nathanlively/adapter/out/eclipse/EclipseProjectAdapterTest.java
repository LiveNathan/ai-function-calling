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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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

@SpringBootTest
@ActiveProfiles("test")
@Tag("database")
class EclipseProjectAdapterTest {
    private EmbeddedStorageManager storageManager;
    private EclipseProjectAdapter adapter;
    private Path storageDir;

    @BeforeEach
    void setUp() {
        storageDir = Paths.get(System.getProperty("java.io.tmpdir"), "test-store-" + UUID.randomUUID());

        // Static factory method to configure and create EmbeddedStorageFoundation
        EmbeddedStorageFoundation<?> foundation = EmbeddedStorageConfiguration.Builder()
                .setStorageDirectory(storageDir.toString())
                .createEmbeddedStorageFoundation();

        // Create and start EmbeddedStorageManager directly in the test
        storageManager = foundation.createEmbeddedStorageManager().start();

        // Set the root object for storageManager
        storageManager.setRoot(new DataRoot());

        // Initialize the adapter with the storageManager
        adapter = new EclipseProjectAdapter(storageManager);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Shutdown the storage manager if running
        if (storageManager != null) {
            storageManager.shutdown();
        }

        // Clean up the temporary storage directory
        if (Files.exists(storageDir)) {
            deleteDirectoryRecursively(storageDir);
        }
    }

    @Test
    void canReadAndWrite() {
        List<Project> projects = adapter.findAll();
        assertThat(projects).isEmpty();

        Project project = Project.create("Project A");
        adapter.save(project);

        List<Project> allProjects = adapter.findAll();
        assertThat(allProjects).hasSize(1);
        assertThat(allProjects.getFirst().name()).isEqualTo("Project A");
    }

    private void deleteDirectoryRecursively(Path path) throws IOException {
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
    }
}