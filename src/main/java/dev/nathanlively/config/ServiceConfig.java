package dev.nathanlively.config;

import dev.nathanlively.adapter.out.ai.SpringAiAdapter;
import dev.nathanlively.application.AiService;
import dev.nathanlively.application.ClockInService;
import dev.nathanlively.application.InMemoryProjectRepository;
import dev.nathanlively.application.InMemoryResourceRepository;
import dev.nathanlively.application.port.AiGateway;
import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public AiService aiService(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        AiGateway gateway = new SpringAiAdapter(chatClientBuilder, chatMemory);
        return new AiService(gateway);
    }

    @Bean
    public ResourceRepository resourceRepository() {
        return InMemoryResourceRepository.createEmpty();
    }

    @Bean
    public ProjectRepository projectRepository() {
        return InMemoryProjectRepository.createEmpty();
    }

    @Bean
    public ClockInService clockInService(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        return new ClockInService(resourceRepository, projectRepository);
    }
}
