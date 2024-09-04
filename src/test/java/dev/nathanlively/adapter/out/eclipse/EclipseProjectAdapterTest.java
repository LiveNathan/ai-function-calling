package dev.nathanlively.adapter.out.eclipse;

import dev.nathanlively.domain.DataRoot;
import dev.nathanlively.domain.Project;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestConfig.class)
@Tag("database")
class EclipseProjectAdapterTest {
    @Autowired
    EmbeddedStorageManager storageManager;
    EclipseProjectAdapter adapter;

    @BeforeEach
    void setUp() {
        // Ensure storage manager is not in the process of starting up
        while (storageManager.isStartingUp()) {
            // Wait for the startup process to complete
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (!storageManager.isRunning()) {
            storageManager.start(); // Start the storage manager if not already running
        }

        if (storageManager.root() == null) {
            storageManager.setRoot(new DataRoot());
        }

        adapter = new EclipseProjectAdapter(storageManager); // Initialize adapter with storageManager
    }

    @AfterEach
    void tearDown() {
        // Ensure storage manager is not in the process of shutting down
        while (storageManager.isShuttingDown()) {
            // Wait for the shutdown process to complete
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (storageManager.isRunning()) {
            storageManager.shutdown(); // Shutdown the storage manager if running
        }
    }

    @Test
    void canReadAndWrite() throws Exception {
        List<Project> projects = adapter.findAll();
        assertThat(projects).isEmpty();

        Project project = Project.create("Project A");
        adapter.save(project);
        List<Project> allProjects = adapter.findAll();
        assertThat(allProjects).hasSize(1);
        assertThat(allProjects.getFirst().name()).isEqualTo("Project A");
    }
}