package dev.nathanlively.adapter.out.eclipse;

import org.eclipse.store.integrations.spring.boot.types.configuration.EclipseStoreProperties;
import org.eclipse.store.integrations.spring.boot.types.factories.EmbeddedStorageFoundationFactory;
import org.eclipse.store.integrations.spring.boot.types.factories.EmbeddedStorageManagerFactory;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageFoundation;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.UUID;

@Configuration
public class TestConfig {

    @Bean
    @Primary
    EmbeddedStorageManager inMemoryStorageManager(
            @Autowired EclipseStoreProperties myConfiguration,
            @Autowired EmbeddedStorageManagerFactory managerFactory,
            @Autowired EmbeddedStorageFoundationFactory foundationFactory) {

        String tempDir = System.getProperty("java.io.tmpdir") + "/test-store-" + UUID.randomUUID();
        myConfiguration.setStorageDirectory(tempDir);
        EmbeddedStorageFoundation<?> storageFoundation = foundationFactory.createStorageFoundation(myConfiguration);

        return managerFactory.createStorage(storageFoundation, true);
    }
}
