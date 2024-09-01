package dev.nathanlively.config;

import dev.nathanlively.adapter.out.ai.SpringAiAdapter;
import dev.nathanlively.application.*;
import dev.nathanlively.application.port.AiGateway;
import dev.nathanlively.application.port.ProjectRepository;
import dev.nathanlively.application.port.ResourceRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    public AiService aiService(ChatClient.Builder chatClientBuilder, ProjectRepository projectRepository, VectorStore vectorStore, ChatMemory chatMemory) {
        AiGateway gateway = new SpringAiAdapter(chatClientBuilder, projectRepository, vectorStore, chatMemory);
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

    @Bean
    public ClockOutService clockOutService(ResourceRepository resourceRepository) {
        return new ClockOutService(resourceRepository);
    }

    @Bean
    public UnfulfilledRequestService unfulfilledRequestService(VectorStore vectorStore) {
        return new UnfulfilledRequestService(vectorStore);
    }

    @Bean
    public UpdateTimesheetEntryService updateTimesheetEntryService(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        return new UpdateTimesheetEntryService(resourceRepository, projectRepository);
    }

    @Bean
    public CreateProjectService createProjectService(ProjectRepository projectRepository) {
        return new CreateProjectService(projectRepository);
    }

    @Bean
    public CreateTimesheetEntryService createTimesheetEntryService(ResourceRepository resourceRepository, ProjectRepository projectRepository) {
        return new CreateTimesheetEntryService(resourceRepository, projectRepository);
    }
}
