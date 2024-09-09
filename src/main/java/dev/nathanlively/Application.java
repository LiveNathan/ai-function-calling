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
//    CommandLineRunner init(ResourceRepository resourceRepository, ProjectRepository projectRepository, UserRepository userRepository) {
//        return args -> {
//            String email = "nathanlively@gmail.com";
//            String name = "Nathan Lively";
//            Resource resource = Resource.create(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, name, email, null);
//            String rawPassword = "password";
//            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//            String hashedPassword = passwordEncoder.encode(rawPassword);
//            User user = new User(email, name, hashedPassword, Collections.singleton(Role.ADMIN), new byte[0]);
//            resourceRepository.save(resource);
//            userRepository.save(user);
//            List<String> projectNames = projectRepository.findAll().stream().map(Project::name).toList();
//            System.out.println("Project names:" + projectNames);
//        };
//    }

}
