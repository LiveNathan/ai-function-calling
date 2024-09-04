package dev.nathanlively.adapter.out.eclipse;

import org.eclipse.store.integrations.spring.boot.types.configuration.EclipseStoreProperties;
import org.eclipse.store.integrations.spring.boot.types.factories.EmbeddedStorageFoundationFactory;
import org.eclipse.store.integrations.spring.boot.types.factories.EmbeddedStorageManagerFactory;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageFoundation;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.atomic.AtomicInteger;

@Tag("database")
class EclipseProjectAdapterTest {

    EmbeddedStorageManager storageManager;


    @Bean
    EmbeddedStorageManager injectStorageTest(
            @Autowired EclipseStoreProperties myConfiguration,
            @Autowired EmbeddedStorageManagerFactory managerFactory,
            @Autowired EmbeddedStorageFoundationFactory foundationFactory
    ) {
        String temp = String.format("./target/tempstorage-%05d", new AtomicInteger(1).getAndIncrement());
        myConfiguration.setStorageDirectory(temp);
        myConfiguration.setAutoStart(false);
        EmbeddedStorageFoundation<?> storageFoundation = foundationFactory.createStorageFoundation(myConfiguration);
        return managerFactory.createStorage(storageFoundation, true);
    }

}