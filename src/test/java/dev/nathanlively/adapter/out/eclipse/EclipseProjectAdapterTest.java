package dev.nathanlively.adapter.out.eclipse;

import dev.nathanlively.domain.Project;
import org.eclipse.store.storage.embedded.types.EmbeddedStorage;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("database")
class EclipseProjectAdapterTest {

    private EclipseProjectAdapter adapter;
    @TempDir
    Path storageDir;
    private static final Logger log = LoggerFactory.getLogger(EclipseProjectAdapterTest.class);

    @Test
    void canWriteReadRestartAndReadAgain() {
        writeData();
        log.info("Storage manager shut down. Restarting...");

        readData();
    }

    private void writeData() {
        try (EmbeddedStorageManager storageManager = startStorageManager()) {
            adapter = new EclipseProjectAdapter(storageManager);
            List<Project> projects = adapter.findAll();
            assertThat(projects).isEmpty();

            Project project = Project.create("Project A");
            adapter.save(project);

            List<Project> allProjects = adapter.findAll();
            assertThat(allProjects).hasSize(1);
            assertThat(allProjects.getFirst().name()).isEqualTo("Project A");
        }
    }

    private void readData() {
        try (EmbeddedStorageManager storageManager = startStorageManager()) {
            adapter = new EclipseProjectAdapter(storageManager);

            List<Project> allProjects = adapter.findAll();
            assertThat(allProjects).hasSize(1);
            assertThat(allProjects.getFirst().name()).isEqualTo("Project A");
        }
    }

    private EmbeddedStorageManager startStorageManager() {
        return EmbeddedStorage.start(storageDir);
    }
}