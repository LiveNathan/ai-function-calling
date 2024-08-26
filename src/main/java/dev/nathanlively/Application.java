package dev.nathanlively;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.domain.JobTitle;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.ResourceType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Theme(value = "ai-function-calling")
@Push
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(ResourceRepository resourceRepository) {
        return args -> {
            Resource resource = new Resource(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", "nathanlively@gmail.com", null);
            resourceRepository.save(resource);
        };
    }

}
