package dev.nathanlively;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import dev.nathanlively.application.port.UserRepository;
import dev.nathanlively.domain.JobTitle;
import dev.nathanlively.domain.Project;
import dev.nathanlively.domain.Resource;
import dev.nathanlively.domain.ResourceType;
import dev.nathanlively.security.Role;
import dev.nathanlively.security.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@Theme(value = "ai-function-calling")
@Push
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(ResourceRepository resourceRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        return args -> {
            String email = "nathanlively@gmail.com";
            String name = "Nathan Lively";
            Resource resource = Resource.withSystemClock(ResourceType.FULL_TIME, JobTitle.TECHNICIAN, name, email, null);
            String rawPassword = "password";
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(rawPassword);
            User user = new User(email, name, hashedPassword, Collections.singleton(Role.ADMIN), new byte[0]);
            resourceRepository.save(resource);
            userRepository.save(user);
            List<String> projectNames = projectRepository.findAll().stream().map(Project::name).toList();
            System.out.println("Project names:" + projectNames);
        };
    }

}
