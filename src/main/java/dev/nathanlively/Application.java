package dev.nathanlively;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(value = "ai-function-calling")
@Push
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    CommandLineRunner init(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
//        return args -> {
//            Resource resource = Resource.withSystemClock(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, "Nathan Lively", "nathanlively@gmail.com", null);
//            resourceRepository.save(resource);
//            projectRepository.save(Project.create("Project A (12345)"));
//            List<String> projectNames = projectRepository.findAll().stream().map(Project::name).toList();
//            System.out.println("Project names:" + projectNames);
//        };
//    }

}
